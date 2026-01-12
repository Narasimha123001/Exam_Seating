package com.techtricks.Exam_Seating.controllers;

import com.techtricks.Exam_Seating.dto.StudentRequest;
import com.techtricks.Exam_Seating.dto.StudentResponse;
import com.techtricks.Exam_Seating.dto.StudentSeatDetails;
import com.techtricks.Exam_Seating.model.SeatAssignment;
import com.techtricks.Exam_Seating.model.Student;
import com.techtricks.Exam_Seating.repository.SeatAssignmentRepository;
import com.techtricks.Exam_Seating.repository.StudentRepository;
import com.techtricks.Exam_Seating.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final SeatAssignmentRepository  seatAssignmentRepository;
    private final StudentService  studentService;
    private final StudentRepository studentRepository;

    @GetMapping("/{studentId}/room")
    public ResponseEntity<?> getRoom(@PathVariable Long studentId , @RequestParam Long sessionId){
        var asg = seatAssignmentRepository.findBySession_SessionId(sessionId).stream()
                .filter(a->a.getStudent().getStudentId().equals(studentId)).findFirst();
        if(asg.isEmpty()) return ResponseEntity.status(404).body("not Assigned");
        SeatAssignment a = asg.get();

        return ResponseEntity.ok(Map.of("roomId", a.getRoom().getRoomId(), "roomName", a.getRoom().getName(), "slot", a.getSession().getSlotCode()));

    }

    @PostMapping("/add")
    public ResponseEntity<Student> addStudent(@RequestBody StudentRequest studentRequest){
        Student saved = studentService.addStudent(studentRequest);
        return ResponseEntity.ok(saved);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id){
        Student st =  studentService.getStudent(id);
        return ResponseEntity.ok(st);
    }

    @GetMapping
    public ResponseEntity<List<Student>> list() {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Student>> addStudentBulk(@RequestBody List<StudentRequest> student){
        List<Student> saved = studentService.addStudentBulk(student);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/subject/{registerNo}")
    public ResponseEntity<StudentResponse> getStudentSubjects(@PathVariable Long registerNo){
        StudentResponse response = studentService.getStudentWithSubjects(registerNo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("student/{regNo}")
    public ResponseEntity<StudentSeatDetails> getIdByRegNo(@PathVariable  Long regNo) {
        return ResponseEntity.ok(studentService.getStudentSeatDetails(regNo));
    }

}
