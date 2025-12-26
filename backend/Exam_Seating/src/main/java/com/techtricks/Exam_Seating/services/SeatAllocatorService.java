package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.model.*;
import com.techtricks.Exam_Seating.repository.SeatAssignmentRepository;
import com.techtricks.Exam_Seating.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    private final BucketServices bucketServices;               // buildBucketsForSessions(List<Long>)
    private final SeatRepository seatRepository;
    private final RotationChecker rotationChecker;            // may be null
    private final AdjacencyChecker adjacencyChecker;          // updated version (see below)
    private final SeatAssignmentRepository seatAssignmentRepository;
    private final RoomService roomService;
/**
 * allocate seating for all sessions (papers) in the same slot
 * sessionIds -> list of examSessionIds represent different papers in the slot
 */


    public MultiAllocationResult allocateForSlot(List<Long> sessionIds){

        // 1:build the buckets first per exam(_paper)
        Map<Long , Queue<Student>> buckets = bucketServices.buildBucketsForSessions(sessionIds);

        int totalStudents = buckets.values()
                .stream()
                .mapToInt(Queue::size)
                .sum();

        //2: auto select room
        List<Long> selectedRoomIds = roomService.selectRoomsForTotalStudents(totalStudents);

        //3: loads Seats and order them (parity ordering)
        List<Seat> seats = seatRepository.getSeatsForRooms(selectedRoomIds);
        List<Seat> orderedSeats = orderSeatsParityFirst(seats);

        //4: in-memory map of assigned seat for adjacency checks
        Map<String , Student> assignedMap = new HashMap<>();

        //5: bookKeeping per session
        Map<Long , Integer> perSessionAssigned = new HashMap<>();

        List<Long> bucketKeys = new ArrayList<>(buckets.keySet());

//        List<String> debugkeys  = buckets.keySet().stream().map(String::valueOf).toList();

        int bucketIndex = 0;
        int assignedCount   = 0;

        for(Seat seat : orderedSeats) {
            for (int i = 0; i < buckets.size(); i++) {
                int idx = (bucketIndex + i) % buckets.size();
                Long sessionBucketId = bucketKeys.get(idx);

                Queue<Student> q = buckets.get(sessionBucketId);
                if (q == null || q.isEmpty()) {
                    continue;
                }

                Student candidate = q.peek();

                if (rotationChecker != null && rotationChecker.violatesRotation(candidate, seat)) {
                    continue;
                }
                if (adjacencyChecker.hasConflict(seat, assignedMap, candidate)) {
                    continue;
                }

                SeatAssignment asg = SeatAssignment.builder()
                        .session(ExamSession.builder().sessionId(sessionBucketId).build())
                        .room(seat.getRoom())
                        .seat(seat)
                        .student(candidate)
                        .status(AssignStatus.ASSIGNED)
                        .assignedAt(java.time.Instant.now().toString())
                        .build();


                seatAssignmentRepository.save(asg);

                String posKey = seat.getRowNo() + "-" + seat.getColNo() + "-" + seat.getRoom();
                assignedMap.put(posKey, candidate);


                q.poll();
                bucketIndex = (idx + 1) % bucketKeys.size();
                assignedCount++;

                perSessionAssigned.merge(sessionBucketId, 1, Integer::sum);

                break;
            }
        }
        return new MultiAllocationResult(
                assignedCount,
                orderedSeats.size(),
                selectedRoomIds,
                perSessionAssigned
        );
    }

    private List<Seat> orderSeatsParityFirst( List<Seat> seats) {
        List<Seat> odd = new ArrayList<>();
        List<Seat> even = new ArrayList<>();

        for (Seat s: seats) {
            if(s.getPosNo() % 2 != 0) odd.add(s);
            else even.add(s);
        }
        List<Seat> ordered = new ArrayList<>(odd.size() + even.size());
        ordered.addAll(odd);
        ordered.addAll(even);
        return ordered;
    }

    public record MultiAllocationResult(int assignedTotal , int seatsTotal , List<Long> selectedRooms, Map<Long,Integer> perSessionAssigned) {}
}