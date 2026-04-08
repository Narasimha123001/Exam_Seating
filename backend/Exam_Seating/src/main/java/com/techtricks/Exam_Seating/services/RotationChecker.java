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

@Service
@RequiredArgsConstructor
@Slf4j
public class RotationChecker {

    private final SeatHistoryRepository seatHistoryRepository;

    /**
     * Load ONLY latest history per student
     */
    public Map<Long, SeatHistory> loadRotationCache(List<Long> studentIds) {

        if (studentIds.isEmpty()) {
            return new HashMap<>();
        }

        log.info("Loading rotation history for {} students", studentIds.size());

        List<SeatHistory> histories =
                seatHistoryRepository.findLatestPerStudent(studentIds);

        // Convert to Map<studentId, SeatHistory>
        Map<Long, SeatHistory> cache = new HashMap<>();
        for (SeatHistory h : histories) {
            cache.put(h.getStudent().getStudentId(), h);
        }

        log.info("Loaded {} history records from database", histories.size());

        return cache;
    }

    /**
     * Check ONLY last seat (no room check)
     */
    public boolean violatesRotation(Student student,
                                    Seat seat,
                                    Map<Long, SeatHistory> cache) {

        SeatHistory lastHistory = cache.get(student.getStudentId());

        if (lastHistory == null) {
            return false; // no history → allow
        }

        // ONLY check seat position
        boolean sameSeat =
                lastHistory.getRowNo() == seat.getRowNo() &&
                        lastHistory.getColNo() == seat.getColNo() &&
                        lastHistory.getPosNo() == seat.getPosNo();

        if (sameSeat) {
            log.trace("Student {} rejected: same seat ({}-{}-{})",
                    student.getRegisterNo(),
                    seat.getRowNo(),
                    seat.getColNo(),
                    seat.getPosNo());
        }

        return sameSeat;
    }
}