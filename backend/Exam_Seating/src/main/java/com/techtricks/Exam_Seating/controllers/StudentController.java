package com.techtricks.Exam_Seating.controllers;
import com.techtricks.Exam_Seating.dto.*;
import com.techtricks.Exam_Seating.model.SeatAssignment;
import com.techtricks.Exam_Seating.model.Student;
import com.techtricks.Exam_Seating.repository.SeatAssignmentRepository;
import com.techtricks.Exam_Seating.repository.StudentRepository;
import com.techtricks.Exam_Seating.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final SeatAssignmentRepository  seatAssignmentRepository;
    private final StudentService  studentService;

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
//        Student saved = studentService.addStudent(studentRequest);
//        return ResponseEntity.ok(saved);
        return ResponseEntity.ok(studentService.addStudent(studentRequest));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id){
        Student st =  studentService.getStudent(id);
        return ResponseEntity.ok(st);
    }
//
//    @GetMapping
//    public ResponseEntity<List<Student>> list() {
//        return ResponseEntity.ok(studentRepository.findAll());
//    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Student>> addStudentBulk(@RequestBody List<StudentRequest> student){
        List<Student> saved = studentService.addStudentBulk(student);
        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/subjects")
    public ResponseEntity<StudentResponse> getStudentSubjects(){
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return ResponseEntity.ok( studentService.getStudentWithSubjects(email));
    }

    @GetMapping("student/{regNo}")
    public ResponseEntity<StudentSeatDetails> getIdByRegNo(@PathVariable  Long regNo) {
        return ResponseEntity.ok(studentService.getStudentSeatDetails(regNo));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentProfileDto> getMyProfile() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return ResponseEntity.ok(
                studentService.getStudentByEmail(email));

    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/all")
//    public ResponseEntity<Page<StudentListResponse>> getAllStudents(@RequestParam(defaultValue = "0") int page , @RequestParam(defaultValue = "20") int size){
//        return ResponseEntity.ok(studentService.list(page, size));
//    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Page<StudentListResponse>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort
    ) {

        return ResponseEntity.ok(
                studentService.list(page, size, search, sort)
        );
    }

    @GetMapping("/list/{year}/deptId/{deptId}")
    public ResponseEntity<List<Long>> getStudentYearWiseList(@PathVariable int year , @PathVariable Long  deptId) {
        return ResponseEntity.ok(studentService.getStudentYearWiseList(year, deptId));
    }
}
