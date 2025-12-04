package com.techtricks.Exam_Seating.controllers;

import com.techtricks.Exam_Seating.dto.ExamSessionCreateRequest;
import com.techtricks.Exam_Seating.dto.ExamSessionResponse;
import com.techtricks.Exam_Seating.model.ExamSession;
import com.techtricks.Exam_Seating.services.ExamSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/examSessions")
public class ExamSessionController {

    private final ExamSessionService examSessionService;

    public ExamSessionController(ExamSessionService examSessionService) {
        this.examSessionService = examSessionService;
    }

    @PostMapping
    public ResponseEntity<ExamSessionResponse> create(@RequestBody ExamSessionCreateRequest request) {
        ExamSessionResponse examSession = examSessionService.create(request);
        return ResponseEntity.ok(examSession);
    }

    @GetMapping("/id/{sessionId}")
    public ResponseEntity<ExamSession> getOne(@PathVariable Long sessionId) {
        return ResponseEntity.ok(examSessionService.getById(sessionId));
    }

    @GetMapping("/{sessionId}/capacity")
    public ResponseEntity<Map<String, Object>> getCapacity(@PathVariable Long sessionId) {
        long capacity = examSessionService.calculateCapacity(sessionId);

        return ResponseEntity.ok(
                Map.of(
                        "sessionId", sessionId,
                        "capacityRequired", capacity
                )
        );
    }

    // ✅ updates capacityRequired field in DB
    @PutMapping("/{sessionId}/capacity/update")
    public ResponseEntity<ExamSession> updateCapacity(@PathVariable Long sessionId) {
        return ResponseEntity.ok(examSessionService.updateCapacityRequired(sessionId));
    }
}
