package com.techtricks.Exam_Seating.controllers;

import com.techtricks.Exam_Seating.dto.SubjectRequest;
import com.techtricks.Exam_Seating.model.Subject;
import com.techtricks.Exam_Seating.repository.SubjectRepository;
import com.techtricks.Exam_Seating.services.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final  SubjectRepository  subjectRepository;
    private final SubjectService subjectService;

    @PostMapping
    public ResponseEntity<Subject> create(@RequestBody Subject sbu){
        return ResponseEntity.ok(subjectRepository.save(sbu));
    }

    @GetMapping
    public ResponseEntity<List<Subject>> getAll(){
        return ResponseEntity.ok(subjectRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subject> getSubjectById(@PathVariable Long id){
        return ResponseEntity.of(subjectRepository.findById(id));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Subject>> addSubjectsBulk(@RequestBody List<SubjectRequest> subjectRequests){
        List<Subject> created = subjectService.addSubjectsBulk(subjectRequests);
        return ResponseEntity.ok(created);
    }
}
