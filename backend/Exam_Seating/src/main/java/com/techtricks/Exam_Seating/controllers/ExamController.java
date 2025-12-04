package com.techtricks.Exam_Seating.controllers;

import com.techtricks.Exam_Seating.model.Exam;
import com.techtricks.Exam_Seating.repository.ExamRepository;
import com.techtricks.Exam_Seating.services.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam")
public class ExamController {
    private final ExamService examService;
    private final ExamRepository examRepository;

    public ExamController(ExamService examService, ExamRepository examRepository) {
        this.examService = examService;
        this.examRepository = examRepository;
    }

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
