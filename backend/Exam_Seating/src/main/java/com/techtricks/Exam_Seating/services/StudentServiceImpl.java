package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.dto.*;
import com.techtricks.Exam_Seating.model.Department;
import com.techtricks.Exam_Seating.model.Student;
import com.techtricks.Exam_Seating.model.Subject;
import com.techtricks.Exam_Seating.repository.DepartmentRepository;
import com.techtricks.Exam_Seating.repository.SeatAssignmentRepository;
import com.techtricks.Exam_Seating.repository.StudentRepository;
import com.techtricks.Exam_Seating.repository.SubjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;
    private final SeatAssignmentRepository seatAssignmentRepository;


    @Override
    public Student addStudent(StudentRequest st) {

        Department d = departmentRepository.findById(st.getDepId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Student s = Student.builder()
                .registerNo(st.getRegistrationId())
                .name(st.getName())
                .year(st.getYear())
                .semester(st.getSem())
                .email(st.getEmail())
                .phone(st.getPhone())
                .department(d)
                .build();
        return studentRepository.save(s);
    }


    @Override
    @Transactional
    public Student updateStudent(StudentRequest st) {

        Department d = departmentRepository.findById(st.getDepId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Student updateStudent = Student.builder()
                .registerNo(st.getRegistrationId())
                .name(st.getName())
                .department(d)
                .year(st.getYear())
                .semester(st.getSem())
                .email(st.getEmail())
                .phone(st.getPhone())

                .build();
        return studentRepository.save(updateStudent);


    }


    @Override
    public Student getStudent(Long registrationId){
        Optional<Student> optional = studentRepository.findByRegisterNo(registrationId);
        return optional.orElse(null);
    }

    @Override
    public List<Student> addStudentBulk(List<StudentRequest> list) {
        List<Student> students = new ArrayList<>();

        for (StudentRequest st : list) {
            Department d = departmentRepository.findById(st.getDepId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));


            Student s = Student.builder()
                    .registerNo(st.getRegistrationId())
                    .name(st.getName())
                    .department(d)
                    .year(st.getYear())
                    .semester(st.getSem())
                    .email(st.getEmail())
                    .phone(st.getPhone())
                    .build();


            students.add(s);

        }
        return studentRepository.saveAll(students);
    }

    @Override
    public StudentResponse getStudentWithSubjects(Long registerNo) {

        Student student = studentRepository.findByRegisterNo(registerNo)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Subject> subjectList = subjectRepository.findByDeptSemYear(
                student.getDepartment().getDeptId(),
                student.getSemester(),
                student.getYear()
        );

        List<SubjectResponse> subjectResponses = subjectList.stream().map(s -> {
            SubjectResponse dto = new SubjectResponse();

            dto.setSubjectCode(s.getSubjectCode());
            dto.setTitle(s.getTitle());
            return dto;
        }).toList();

        StudentResponse res = new StudentResponse();
        res.setRegistrationId(student.getRegisterNo());
        res.setName(student.getName());
        res.setYear(student.getYear());
        res.setSemester(student.getSemester());
        res.setSubjects(subjectResponses);

        return res;
    }

    @Override
    public StudentSeatDetails getStudentSeatDetails(Long registerNo) {
        Student student =studentRepository.findByRegisterNo(registerNo)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        long roomId= seatAssignmentRepository.findRoomNoByStudentStudentId(student.getStudentId());
        StudentSeatDetails studentSeatDetails = new StudentSeatDetails();
        studentSeatDetails.setRegisterNo(student.getRegisterNo());
        studentSeatDetails.setRoomNo(roomId);

        return  studentSeatDetails;

    }

    @Override
    public StudentProfileDto getStudentByEmail(String email) {
        Student student =  studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));


        return new  StudentProfileDto(
                student.getRegisterNo(),
                student.getName(),
                student.getEmail(),
                student.getDepartment().getName(), // safely accessed
                student.getYear(),
                student.getSemester()
        );
    }
}
