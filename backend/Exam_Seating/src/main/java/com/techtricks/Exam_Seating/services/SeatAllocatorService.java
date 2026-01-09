package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.exception.AllocationAlreadyExistsException;
import com.techtricks.Exam_Seating.exception.AllocationException;
import com.techtricks.Exam_Seating.exception.DuplicateRegistrationException;
import com.techtricks.Exam_Seating.exception.InsufficientSeatsException;
import com.techtricks.Exam_Seating.model.*;
import com.techtricks.Exam_Seating.repository.SeatAssignmentRepository;
import com.techtricks.Exam_Seating.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

/**
 * Final bench-wise allocator. Uses buckets keyed by examSessionId.
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatAllocatorService {

    private final BucketServices bucketServices;
    private final RoomService roomService;
    private final SeatRepository seatRepository;
    private final SeatAssignmentRepository seatAssignmentRepository;
    private final RotationChecker rotationChecker;

    /**
     * MAIN ENTRY – Slot wise allocation
     */
    @Transactional
    public MultiAllocationResult allocateForSlot(List<Long> sessionIds) {

        long startTime = System.currentTimeMillis();
        log.info("==== SEAT ALLOCATION STARTED ====");
        log.info("Sessions received for allocation: {}", sessionIds);

        // ---------------- PHASE 1: BUILD BUCKETS ----------------
        Map<Long, Queue<Student>> buckets =
                bucketServices.buildBucketsForSessions(sessionIds);

        int totalStudents = buckets.values()
                .stream()
                .mapToInt(Queue::size)
                .sum();

        log.info("Buckets built: {}, Total students: {}",
                buckets.size(), totalStudents);

        if (totalStudents == 0) {
            log.warn("No students found for given sessions");
            return new MultiAllocationResult(0, 0, List.of(), Map.of(), Map.of());
        }

        // ---------------- PHASE 2: ROOM SELECTION ----------------
        List<Long> roomIds =
                roomService.selectRoomsForTotalStudents(totalStudents);

        log.info("Selected rooms: {}", roomIds);

        List<Seat> seats =
                seatRepository
                        .findByRoom_RoomIdInOrderByRoom_RoomIdAscBenchIndexAscPosNoAsc(roomIds);

        log.info("Loaded {} seats across selected rooms", seats.size());

        // ---------------- PHASE 3: ROTATION CACHE ----------------
        List<Long> allStudentIds = buckets.values().stream()
                .flatMap(Queue::stream)
                .map(Student::getStudentId)
                .distinct()
                .toList();

        Map<Long, List<SeatHistory>> rotationCache =
                rotationChecker.loadRotationCache(allStudentIds);

        log.info("Rotation cache loaded for {} students", allStudentIds.size());

        // ---------------- PHASE 4: BENCH‑WISE ALLOCATION ----------------
        Map<Long, Integer> perSessionAssigned = new HashMap<>();

        int assignedCount = 0;
        int rotationSkips = 0;
        int emptyBucketSkips = 0;

        int i = 0;
        int benchIndex = 1;

        while (i < seats.size()) {

            Seat left = seats.get(i);
            Seat middle = (i + 1 < seats.size()) ? seats.get(i + 1) : null;
            Seat right = (i + 2 < seats.size()) ? seats.get(i + 2) : null;

            int activeBuckets = countActiveBuckets(buckets);

            log.debug("Bench {} | Active buckets: {}", benchIndex, activeBuckets);

            if (activeBuckets >= 3) {
                assignedCount += fillBenchCase3(
                        left, middle, right,
                        buckets, rotationCache, perSessionAssigned
                );
            }
            else if (activeBuckets == 2) {
                assignedCount += fillBenchCase2(
                        left, right,
                        buckets, rotationCache, perSessionAssigned
                );
            }
            else if (activeBuckets == 1) {
                assignedCount += fillBenchCase1(
                        left, right,
                        buckets, rotationCache, perSessionAssigned
                );
            }
            else {
                log.info("All buckets exhausted at bench {}", benchIndex);
                break;
            }

            i += 3;
            benchIndex++;
        }

        long duration = System.currentTimeMillis() - startTime;

        Map<String, Object> metrics = Map.of(
                "durationMs", duration,
                "totalStudents", totalStudents,
                "assignedCount", assignedCount,
                "unassigned", totalStudents - assignedCount,
                "utilizationPercent",
                Math.round((assignedCount * 100.0) / seats.size())
        );

        log.info("==== SEAT ALLOCATION COMPLETED ====");
        log.info("Assigned: {}/{} seats", assignedCount, seats.size());
        log.info("Metrics: {}", metrics);

        return new MultiAllocationResult(
                assignedCount,
                seats.size(),
                roomIds,
                perSessionAssigned,
                metrics
        );
    }

    // ============================================================
    // CASE‑1: 3 OR MORE BUCKETS → L, M, R
    // ============================================================
    private int fillBenchCase3(
            Seat l, Seat m, Seat r,
            Map<Long, Queue<Student>> buckets,
            Map<Long, List<SeatHistory>> cache,
            Map<Long, Integer> stats) {

        List<Long> active = getActiveBucketIds(buckets, 3);
        if (active.size() < 3) return 0;

        log.trace("Case‑3 buckets used: {}", active);

        int count = 0;
        count += assign(l, active.get(0), buckets, cache, stats);
        count += assign(m, active.get(1), buckets, cache, stats);
        count += assign(r, active.get(2), buckets, cache, stats);
        return count;
    }

    // ============================================================
    // CASE‑2: EXACTLY 2 BUCKETS → L & R
    // ============================================================
    private int fillBenchCase2(
            Seat l, Seat r,
            Map<Long, Queue<Student>> buckets,
            Map<Long, List<SeatHistory>> cache,
            Map<Long, Integer> stats) {

        List<Long> active = getActiveBucketIds(buckets, 2);
        if (active.size() < 2) return 0;

        log.trace("Case‑2 buckets used: {}", active);

        int count = 0;
        count += assign(l, active.get(0), buckets, cache, stats);
        count += assign(r, active.get(1), buckets, cache, stats);
        return count;
    }

    // ============================================================
    // CASE‑3: ONLY 1 BUCKET → L & R (same paper)
    // ============================================================
    private int fillBenchCase1(
            Seat l, Seat r,
            Map<Long, Queue<Student>> buckets,
            Map<Long, List<SeatHistory>> cache,
            Map<Long, Integer> stats) {

        List<Long> active = getActiveBucketIds(buckets, 1);
        if (active.isEmpty()) return 0;

        Long bucketId = active.get(0);
        log.trace("Case‑1 single bucket used: {}", bucketId);

        int count = 0;
        count += assign(l, bucketId, buckets, cache, stats);
        count += assign(r, bucketId, buckets, cache, stats);
        return count;
    }

    // ============================================================
    // ASSIGN ONE STUDENT TO ONE SEAT
    // ============================================================
    private int assign(
            Seat seat,
            Long bucketId,
            Map<Long, Queue<Student>> buckets,
            Map<Long, List<SeatHistory>> cache,
            Map<Long, Integer> stats) {

        Queue<Student> q = buckets.get(bucketId);
        if (q == null || q.isEmpty()) {
            return 0;
        }

        Student student = q.peek();

        if (rotationChecker.violatesRotation(student, seat, cache)) {
            log.trace("Rotation rejected student {} for seat {}",
                    student.getRegisterNo(), seat.getSeatLabel());
            return 0;
        }

        seatAssignmentRepository.save(
                SeatAssignment.builder()
                        .seat(seat)
                        .room(seat.getRoom())
                        .student(student)
                        .session(ExamSession.builder()
                                .sessionId(bucketId)
                                .build())
                        .status(AssignStatus.ASSIGNED)
                        .assignedAt(Instant.now().toString())
                        .build()
        );

        q.poll();
        stats.merge(bucketId, 1, Integer::sum);

        log.trace("Assigned student {} to {} (session {})",
                student.getRegisterNo(),
                seat.getSeatLabel(),
                bucketId);

        return 1;
    }

    // ============================================================
    private int countActiveBuckets(Map<Long, Queue<Student>> buckets) {
        return (int) buckets.values().stream()
                .filter(q -> !q.isEmpty())
                .count();
    }

    private List<Long> getActiveBucketIds(
            Map<Long, Queue<Student>> buckets, int limit) {

        return buckets.entrySet().stream()
                .filter(e -> !e.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .limit(limit)
                .toList();
    }

    /**
     * Get allocation status for sessions
     */
    public Map<String, Object> getAllocationStatus(List<Long> sessionIds) {
        long totalAssignments = seatAssignmentRepository.countBySessionIdIn(sessionIds);

        Map<Long, Long> perSessionCount = seatAssignmentRepository
                .countPerSession(sessionIds);

        return Map.of(
                "totalAssignments", totalAssignments,
                "sessionsAllocated", perSessionCount.size(),
                "perSessionCount", perSessionCount,
                "isAllocated", totalAssignments > 0
        );
    }

    // ============================================================
    public record MultiAllocationResult(
            int assignedTotal,
            int seatsTotal,
            List<Long> selectedRooms,
            Map<Long, Integer> perSessionAssigned,
            Map<String, Object> metrics
    ) {}
}
