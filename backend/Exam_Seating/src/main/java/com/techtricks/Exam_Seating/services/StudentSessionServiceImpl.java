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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentSessionServiceImpl implements StudentSessionService {

    private final StudentRepository studentRepository;
    private final ExamSessionRepository examSessionRepository;
    private final StudentSessionRepository studentSessionRepository;

    @Override
    @Transactional
    public StudentRegisterResponse register(StudentRegisterRequest request) {

        Student student = studentRepository.findByRegisterNo(request.getRegistrationNo())
                .orElseThrow(() -> new RuntimeException(
                        "Student not found: " + request.getRegistrationNo()));

        ExamSession session = examSessionRepository.findBySessionId(request.getSessionId());

        if (session == null) {
            throw new RuntimeException("Session not found: " + request.getSessionId());
        }

        // Save registration
        studentSessionRepository.save(
                StudentSession.builder()
                        .student(student)
                        .session(session)
                        .build()
        );

        log.info("Registered student {} for session {}",
                student.getRegisterNo(), session.getSessionId());

        StudentRegisterResponse response = new StudentRegisterResponse();
        response.setSessionId(request.getSessionId());
        response.setRegistrationNo(List.of(request.getRegistrationNo()));

        return response;
    }

    @Override
    @Transactional
    public StudentRegisterResponse bulkRegister(StudentBulkRegisterRequest request) {

        ExamSession session = examSessionRepository.findBySessionId(request.getSessionId());

        if (session == null) {
            throw new RuntimeException("Session not found: " + request.getSessionId());
        }

        List<StudentSession> toSave = new ArrayList<>();
        List<Long> registered = new ArrayList<>();
        List<Long> failed = new ArrayList<>();

        for (Long regNo : request.getRegistrationIds()) {
            studentRepository.findByRegisterNo(regNo).ifPresentOrElse(
                    student -> {
                        toSave.add(
                                StudentSession.builder()
                                        .student(student)
                                        .session(session)
                                        .build()
                        );
                        registered.add(regNo);
                    },
                    () -> {
                        log.warn("Student not found: {}", regNo);
                        failed.add(regNo);
                    }
            );
        }

        // Batch save
        studentSessionRepository.saveAll(toSave);

        log.info("Bulk registration: {} succeeded, {} failed",
                registered.size(), failed.size());

        StudentRegisterResponse response = new StudentRegisterResponse();
        response.setSessionId(request.getSessionId());
        response.setRegistrationNo(registered);
        response.setFailed(failed);

        return response;
    }
}