package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.dto.StudentRequest;
import com.techtricks.Exam_Seating.dto.StudentResponse;
import com.techtricks.Exam_Seating.dto.StudentSeatDetails;
import com.techtricks.Exam_Seating.dto.SubjectRequest;
import com.techtricks.Exam_Seating.model.Student;
import com.techtricks.Exam_Seating.model.Subject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService {

   // public Student addStudent(Long regNo, String name, Long deptId, int year, int sem);
    public Student addStudent(StudentRequest studentRequest);

    public Student getStudent(Long registrationId);

    List<Student> addStudentBulk(List<StudentRequest> list);


    public StudentResponse getStudentWithSubjects(Long registerNo);

    StudentSeatDetails getStudentSeatDetails(Long registerNo);
}
