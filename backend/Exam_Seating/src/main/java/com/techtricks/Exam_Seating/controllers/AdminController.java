package com.techtricks.Exam_Seating.controllers;

import com.techtricks.Exam_Seating.dto.AllocationResponse;
import com.techtricks.Exam_Seating.exception.AllocationException;
import com.techtricks.Exam_Seating.model.ExamSession;
import com.techtricks.Exam_Seating.repository.ExamSessionRepository;
import com.techtricks.Exam_Seating.services.SeatAllocatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final ExamSessionRepository examSessionRepository;
    private final SeatAllocatorService seatAllocatorService;

    /**
     * Allocate seating for all exam sessions in the given slot (date + startTime)
     * Example: POST /admin/seating/generate?date=2025-11-29&startTime=09:30
     */
    @PostMapping("/generate")
    public ResponseEntity<?> generateBySlot(
            @RequestParam String date,
            @RequestParam String startTime) {

        try {
            log.info("Allocation request received for slot: {} {}", date, startTime);

            // Validate input
            if (date == null || date.isEmpty() || startTime == null || startTime.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Date and startTime are required"));
            }

            // Find all sessions in this slot
            List<ExamSession> sessions = examSessionRepository
                    .findByDateAndStartTime(date, startTime);

            if (sessions.isEmpty()) {
                log.warn("No sessions found for slot: {} {}", date, startTime);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "No exam sessions found for this slot"));
            }

            // Extract session IDs
            List<Long> sessionIds = sessions.stream()
                    .map(ExamSession::getSessionId)
                    .toList();

            log.info("Found {} sessions in slot: {}", sessionIds.size(), sessionIds);

            // Perform allocation
            SeatAllocatorService.MultiAllocationResult result =
                    seatAllocatorService.allocateForSlot(sessionIds);

            log.info("Allocation completed. Assigned: {}/{} seats",
                    result.assignedTotal(), result.seatsTotal());

            // Build response
            AllocationResponse response = AllocationResponse.builder()
                    .slotDate(date)
                    .slotTime(startTime)
                    .sessionsAllocated(sessionIds.size())
                    .sessionIds(sessionIds)
                    .selectedRooms(result.selectedRooms())
                    .assignedTotal(result.assignedTotal())
                    .seatsTotal(result.seatsTotal())
                    .unassignedSeats(result.seatsTotal() - result.assignedTotal())
                    .perSessionAssigned(result.perSessionAssigned())
                    .allocationMetrics(result.metrics())
                    .build();

            return ResponseEntity.ok(response);

        } catch (AllocationException e) {
            log.error("Allocation failed for slot {} {}: {}", date, startTime, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));

        } catch (Exception e) {
            log.error("Unexpected error during allocation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error: " + e.getMessage()));
        }
    }

    /**
     * Get allocation status for a slot
     */
    @GetMapping("/status")
    public ResponseEntity<?> getSlotStatus(
            @RequestParam String date,
            @RequestParam String startTime) {

        List<ExamSession> sessions = examSessionRepository
                .findByDateAndStartTime(date, startTime);

        if (sessions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No sessions found"));
        }

        List<Long> sessionIds = sessions.stream()
                .map(ExamSession::getSessionId)
                .toList();

        Map<String, Object> status = seatAllocatorService.getAllocationStatus(sessionIds);

        return ResponseEntity.ok(status);
    }
}