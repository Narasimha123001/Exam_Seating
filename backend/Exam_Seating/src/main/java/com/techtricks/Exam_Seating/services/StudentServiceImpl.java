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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        Department d = departmentRepository.findDepartmentByDeptId(st.getDepId())
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

        Department d = departmentRepository.findDepartmentByName(st.getName())
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
    public Page<StudentListResponse> list(int page, int size) {

        page = Math.max(page, 0);
        size = Math.min(Math.max(size, 1), 100);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Order.desc("registerNo"),
                        Sort.Order.desc("studentId")) //for pagination
        );

        Page<Student> studentPage = studentRepository.findAll(pageable);

        return studentPage.map(student ->
                new StudentListResponse(
                        student.getRegisterNo(),
                        student.getName(),
                        student.getEmail()
                )
        );
    }


    @Override
    public Page<StudentListResponse> list(
            int page,
            int size,
            String search,
            String sort
    ) {

        page = Math.max(page, 0);
        size = Math.min(Math.max(size, 1), 100);

        Sort sorting;

        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",");
            sorting = parts.length == 2 && parts[1].equalsIgnoreCase("desc")
                    ? Sort.by(parts[0]).descending()
                    : Sort.by(parts[0]).ascending();
        } else {
            sorting = Sort.by("registerNo").descending();
        }

        Pageable pageable = PageRequest.of(page, size, sorting);

        Page<Student> studentPage =
                studentRepository.searchStudents(search, pageable);

        return studentPage.map(student ->
                new StudentListResponse(
                        student.getRegisterNo(),
                        student.getName(),
                        student.getEmail()
                )
        );
    }

    @Override
    public List<Student> addStudentBulk(List<StudentRequest> list) {
        List<Student> students = new ArrayList<>();

        for (StudentRequest st : list) {
            Department d = departmentRepository.findDepartmentByName(st.getName())
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
    public StudentResponse getStudentWithSubjects(String email) {

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        //-> subject form
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
        // up to this
        StudentResponse studentSubjectsResponse = new StudentResponse();
        studentSubjectsResponse.setEmail(email);
//        res.setRegistrationId(student.getRegisterNo());
//        res.setName(student.getName());
//        res.setYear(student.getYear());
//        res.setSemester(student.getSemester());
        studentSubjectsResponse.setSubjects(subjectResponses);

        return studentSubjectsResponse;
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
                student.getPhone(),
                student.getDepartment().getName(), // safely accessed
                student.getYear(),
                student.getSemester()
        );
    }
    @Override
    public List<Long> getStudentYearWiseList(int year , Long  deptId) {
        return studentRepository
                .getRegisterNumbersByYear(year ,  deptId)
                .stream()
                .toList();
    }
}
