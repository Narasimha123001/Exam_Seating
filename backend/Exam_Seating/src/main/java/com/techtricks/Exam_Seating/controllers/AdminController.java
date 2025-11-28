package com.techtricks.Exam_Seating.controllers;

import com.techtricks.Exam_Seating.model.ExamSession;
import com.techtricks.Exam_Seating.repository.ExamSessionRepository;
import com.techtricks.Exam_Seating.services.SeatAllocatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ExamSessionRepository examSessionRepository;
    private final SeatAllocatorService seatAllocatorService;

    public AdminController(ExamSessionRepository examSessionRepository, SeatAllocatorService seatAllocatorService) {
        this.examSessionRepository = examSessionRepository;
        this.seatAllocatorService = seatAllocatorService;
    }

    /**
     * Allocate seating for all exam sessions in the given slot (date + startTime).
     * Example: POST /admin/generate-seating-by-slot?date=2025-11-29&startTime=09:30
     *
     */



    @PostMapping("/generate")
    public ResponseEntity<?> generateBySlot(
            @RequestParam String date,
            @RequestParam String startTime){
        System.out.println(date + " " + startTime);

        List<ExamSession> sessions = examSessionRepository.findByDateAndStartTime(date, startTime);
      //  System.out.println(sessions);
        if(sessions.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Session Not Found"));
        }

        List<Long> sessionIds = sessions.stream()
                .map(ExamSession::getSessionId)
                .toList();
        System.out.println("sessionIds = " + sessionIds);

        SeatAllocatorService.MultiAllocationResult result = seatAllocatorService.allocateForSlot(sessionIds);


        return ResponseEntity.ok(Map.of(
                "slotDate", date,
                "slotTime", startTime,
                "sessionsAllocated", sessionIds.size(),
                "selectedRooms", result.selectedRooms(),
                "assignedTotal", result.assignedTotal(),
                "seatsTotal", result.seatsTotal(),
                "perSessionAssigned", result.perSessionAssigned()
        ));

    }

}
