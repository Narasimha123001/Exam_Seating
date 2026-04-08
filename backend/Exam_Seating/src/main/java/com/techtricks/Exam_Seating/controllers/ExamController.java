package com.techtricks.Exam_Seating.controllers;

import com.techtricks.Exam_Seating.exception.ExamNotFoundExceptions;
import com.techtricks.Exam_Seating.model.Exam;
import com.techtricks.Exam_Seating.repository.ExamRepository;
import com.techtricks.Exam_Seating.services.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;
    private final ExamRepository examRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Exam> createExam(@RequestBody Exam exam) {
        return ResponseEntity.ok(examService.createExam(exam));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Exam>> getAllExams() {
        return ResponseEntity.ok(examRepository.findAll());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<Exam> updateExam(@RequestBody Exam exam) throws ExamNotFoundExceptions {
        return ResponseEntity.ok(examService.updateExam(exam));
    }
}
