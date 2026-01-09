package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.model.Seat;
import com.techtricks.Exam_Seating.model.SeatHistory;
import com.techtricks.Exam_Seating.model.Student;
import com.techtricks.Exam_Seating.repository.SeatHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RotationChecker {

    private final SeatHistoryRepository seatHistoryRepository;

    private static final int HISTORY_LIMIT = 5;

    /**
     * Preload rotation histories for all students in ONE query
     * This replaces thousands of individual DB queries
     */
    public Map<Long, List<SeatHistory>> loadRotationCache(List<Long> studentIds) {

        if (studentIds.isEmpty()) {
            return new HashMap<>();
        }

        log.info("Loading rotation history for {} students", studentIds.size());

        // Single query to fetch top 5 histories per student
        List<SeatHistory> allHistories = seatHistoryRepository
                .findTop5PerStudent(studentIds);

        // Group by student ID
        Map<Long, List<SeatHistory>> cache = allHistories.stream()
                .collect(Collectors.groupingBy(
                        h -> h.getStudent().getStudentId(),
                        Collectors.toList()
                ));

        log.info("Loaded {} history records from database", allHistories.size());

        return cache;
    }

    /**
     * Check if student-seat combination violates rotation rules
     * Uses pre-loaded cache (no DB query)
     */
    public boolean violatesRotation(Student student, Seat seat,
                                    Map<Long, List<SeatHistory>> cache) {

        List<SeatHistory> histories = cache.get(student.getStudentId());

        if (histories == null || histories.isEmpty()) {
            return false; // No history, no violation
        }

        for (SeatHistory history : histories) {

            // Check if same room
            if (history.getRoom() != null && seat.getRoom() != null &&
                    history.getRoom().getRoomId().equals(seat.getRoom().getRoomId())) {
                log.trace("Student {} rejected: same room ({})",
                        student.getRegisterNo(), seat.getRoom().getRoomId());
                return true;
            }

            // Check if exact same seat position
            if (history.getRowNo() == seat.getRowNo() &&
                    history.getColNo() == seat.getColNo() &&
                    history.getPosNo() == seat.getPosNo()) {
                log.trace("Student {} rejected: same position ({}-{}-{})",
                        student.getRegisterNo(), seat.getRowNo(),
                        seat.getColNo(), seat.getPosNo());
                return true;
            }
        }

        return false;
    }
}