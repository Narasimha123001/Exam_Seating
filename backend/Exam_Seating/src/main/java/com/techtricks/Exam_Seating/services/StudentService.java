package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.dto.*;
import com.techtricks.Exam_Seating.model.Student;
import org.springframework.data.domain.Page;

import java.util.List;


public interface StudentService {

   // public Student addStudent(Long regNo, String name, Long deptId, int year, int sem);
     Student addStudent(StudentRequest studentRequest);

   Student getStudent(Long registrationId);

   Page<StudentListResponse> list(int page , int size);
    List<Student> addStudentBulk(List<StudentRequest> list);

    Student updateStudent(StudentRequest studentRequest);

   StudentResponse getStudentWithSubjects(String email);

    StudentSeatDetails getStudentSeatDetails(Long registerNo);

    StudentProfileDto getStudentByEmail(String email);

    List<Long> getStudentYearWiseList(int year ,  Long  deptId);
    Page<StudentListResponse> list(
            int page,
            int size,
            String search,
            String sort
    );
}
