package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.dto.StudentBulkRegisterRequest;
import com.techtricks.Exam_Seating.dto.StudentRegisterRequest;
import com.techtricks.Exam_Seating.dto.StudentRegisterResponse;
import com.techtricks.Exam_Seating.model.ExamSession;
import com.techtricks.Exam_Seating.model.Student;
import com.techtricks.Exam_Seating.model.StudentSession;
import com.techtricks.Exam_Seating.repository.ExamSessionRepository;
import com.techtricks.Exam_Seating.repository.StudentRepository;
import com.techtricks.Exam_Seating.repository.StudentSessionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class StudentSessionServiceImpl implements StudentSessionService {
    private final StudentRepository studentRepository;

    private final ExamSessionRepository examSessionRepository;

    private final StudentSessionRepository  studentSessionRepository;
    private final ExamSessionService examSessionService;

    public StudentSessionServiceImpl(StudentRepository studentRepository, ExamSessionRepository examSessionRepository, StudentSessionRepository studentSessionRepository, ExamSessionService examSessionService) {
        this.studentRepository = studentRepository;
        this.examSessionRepository = examSessionRepository;
        this.studentSessionRepository = studentSessionRepository;
        this.examSessionService = examSessionService;
    }

    @Override
    public StudentRegisterResponse register(StudentRegisterRequest request) {

       // System.out.println(request.getRegistrationNo());
        Student student = studentRepository.findByRegisterNo(request.getRegistrationNo())
                .orElseThrow(()-> new RuntimeException("Student Not Found"));
        //System.out.println(student);
        ExamSession examSession = examSessionRepository.findBySessionId(request.getSessionId());
       // System.out.println(request.getSessionId());
       // System.out.println(examSession);

        studentSessionRepository.save(StudentSession.builder()
                .student(student)
                .session(examSession)
                .build());


        List<Long> registerNo = studentSessionRepository

                .findStudentsBySession(Collections.singletonList(request.getSessionId()))
                .stream()
                .map(Student::getRegisterNo)
                .toList();


        StudentRegisterResponse response = new StudentRegisterResponse();
        response.setSessionId(request.getSessionId());
        response.setRegistrationNo(registerNo);
        return response;
    }

    @Override
    public StudentRegisterResponse bulkRegister(StudentBulkRegisterRequest request) {
        ExamSession session = examSessionRepository.findBySessionId(request.getSessionId());

        List<StudentSession> toSave = new ArrayList<>();
        List<Long> registered = new ArrayList<>();
        List<Long> failed = new ArrayList<>();

        for (Long regNo : request.getRegistrationIds()) {
            studentRepository.findByRegisterNo(regNo).ifPresentOrElse(student -> {

                toSave.add(StudentSession.builder()
                        .student(student)
                        .session(session)
                        .build()
                );
                registered.add(regNo);
            }, () -> failed.add(regNo));
        }
        studentSessionRepository.saveAll(toSave);
        //todo add update here only
        examSessionService.updateCapacityRequired(request.getSessionId());

        StudentRegisterResponse response = new StudentRegisterResponse();
        response.setSessionId(request.getSessionId());
        response.setRegistrationNo(registered);
        response.setFailed(failed);
        return response;
    }
}
