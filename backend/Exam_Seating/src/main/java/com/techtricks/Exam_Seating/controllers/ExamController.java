package com.techtricks.Exam_Seating.controllers;

import com.techtricks.Exam_Seating.model.Exam;
import com.techtricks.Exam_Seating.repository.ExamRepository;
import com.techtricks.Exam_Seating.services.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;
    private final ExamRepository examRepository;

    @PostMapping("/create")
    public ResponseEntity<Exam> createExam(@RequestBody Exam exam) {
        Exam createdExam = examService.createExam(exam);
        return ResponseEntity.ok(createdExam);
    }

    @GetMapping
    public ResponseEntity<List<Exam>> getAllExams() {
        return ResponseEntity.ok(examRepository.findAll());
    }
}
