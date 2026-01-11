package com.techtricks.Exam_Seating.controllers;

import com.techtricks.Exam_Seating.dto.StudentBulkRegisterRequest;
import com.techtricks.Exam_Seating.dto.StudentRegisterRequest;
import com.techtricks.Exam_Seating.dto.StudentRegisterResponse;
import com.techtricks.Exam_Seating.model.Student;
import com.techtricks.Exam_Seating.model.StudentSession;
import com.techtricks.Exam_Seating.repository.StudentRepository;
import com.techtricks.Exam_Seating.repository.StudentSessionRepository;
import com.techtricks.Exam_Seating.services.StudentSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/studentSession")
public class StudentSessionController {
//    private final StudentRepository studentRepository;
    private final StudentSessionService studentSessionService;

    public StudentSessionController(StudentSessionService studentSessionService) {

        this.studentSessionService = studentSessionService;
    }

    @PostMapping("/bulk")
    public ResponseEntity<StudentRegisterResponse> register(@RequestBody StudentBulkRegisterRequest request){
        StudentRegisterResponse response = studentSessionService.bulkRegister(request);
        return ResponseEntity.ok(response);
    }



//    @GetMapping("/{registerNo}")
//    public String findByRegisterNo(@PathVariable Long registerNo) {
//    Student name  = studentRepository.findByRegisterNo(registerNo).orElseThrow();
//    return name.getName();
//    }

    @PostMapping("/register")
    public ResponseEntity<StudentRegisterResponse> registerStudent(@RequestBody StudentRegisterRequest request) {
        StudentRegisterResponse response = studentSessionService.register(request);
        return ResponseEntity.ok(response);
    }


}
