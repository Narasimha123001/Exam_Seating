package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.dto.ExamSessionCreateRequest;
import com.techtricks.Exam_Seating.dto.ExamSessionResponse;
import com.techtricks.Exam_Seating.model.Department;
import com.techtricks.Exam_Seating.model.Exam;
import com.techtricks.Exam_Seating.model.ExamSession;
import com.techtricks.Exam_Seating.model.Subject;
import com.techtricks.Exam_Seating.repository.ExamRepository;
import com.techtricks.Exam_Seating.repository.ExamSessionRepository;
import com.techtricks.Exam_Seating.repository.StudentSessionRepository;
import com.techtricks.Exam_Seating.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExamSessionServiceImpl implements ExamSessionService {

    private final ExamSessionRepository examSessionRepository;
    private final StudentSessionRepository studentSessionRepository;

    private final SubjectRepository subjectRepository;
    private final ExamRepository examRepository;

    public ExamSessionServiceImpl(ExamSessionRepository examSessionRepository, StudentSessionRepository studentSessionRepository, SubjectRepository subjectRepository, ExamRepository examRepository) {
        this.examSessionRepository = examSessionRepository;
        this.studentSessionRepository = studentSessionRepository;
        this.subjectRepository = subjectRepository;
        this.examRepository = examRepository;
    }




    @Override
    public List<ExamSession> findAll() {
        return examSessionRepository.findAll();
    }

    @Override
    public ExamSession getById(Long sessionId) {
        return examSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("session not found"));
    }

    @Override
    public List<ExamSessionResponse> findSessionByExamId(Long examId) {
        List<ExamSession> sessions = examSessionRepository.findExamSessionByExamId(examId);
        return sessions.stream()
                .map(this::map)
                .toList();
    }


    @Transactional
    @Override
    public Long calculateCapacity(Long sessionId) {
        ExamSession examSession = getById(sessionId);
        long count= studentSessionRepository.countBySession_SessionId(sessionId);
        examSession.setCapacityRequired((int) count);
        return count;
    }


    private ExamSessionResponse map(ExamSession session){
        ExamSessionResponse response = new ExamSessionResponse();
        response.setSessionId(session.getSessionId());
        response.setName(session.getExam().getName());
        response.setSubject_code(session.getSubject().getSubjectCode());
        response.setSubject_title(session.getSubject().getTitle());
        response.setDate(session.getDate());
        response.setYear(session.getYear());
        response.setDeptId(session.getDept().getDeptId());
        response.setStartTime(session.getStartTime());
        response.setCapacityRequired(session.getCapacityRequired());
        response.setEndTime(session.getEndTime());
        response.setSlotCode(session.getSlotCode());
        response.setPartNo(session.getPartNo());
        return response;
    }



    @Override
    @Transactional
    public ExamSessionResponse  create(ExamSessionCreateRequest dto) {




        Exam exam = examRepository .findById(dto.getExamId())
                .orElseThrow(() -> new RuntimeException("Exam not found: " + dto.getExamId()));
        Subject subject = subjectRepository.findExamBySubjectCode(dto.getSubjectCode())
                .orElseThrow(() -> new RuntimeException("Subject not found: " + dto.getSubjectCode()));
        Department dept = subject.getDepartments()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No department linked to subject"));

        ExamSession session = ExamSession.builder()
                .exam(exam)
                .subject(subject)
                .subject_code(subject.getSubjectCode())

                .date(dto.getDate())
                .slotCode(dto.getSlotCode())
                .startTime(dto.getStartTime())
                .year(subject.getYear())

                .endTime(dto.getEndTime())
                .partNo(dto.getPartNo())
                // capacityRequired default = 0 (from entity)
                .build();
        //  System.out.println(session);

        session.setDept(dept);
        ExamSession saved =  examSessionRepository.save(session);


        return mapToResponse(saved);
    }
    private ExamSessionResponse mapToResponse(ExamSession session) {

        ExamSessionResponse response = new ExamSessionResponse();
        response.setSessionId(session.getSessionId());
        response.setName(session.getExam().getName()); // depends on your Exam entity
        response.setSubject_title(session.getSubject().getTitle());
        response.setSubject_code(session.getSubject().getSubjectCode());
        response.setDate(session.getDate());
        response.setCapacityRequired(session.getCapacityRequired());
        response.setSlotCode(session.getSlotCode());
        response.setStartTime(session.getStartTime());
        response.setDeptId(session.getDept().getDeptId());
        response.setEndTime(session.getEndTime());
        response.setDeptId(session.getDept().getDeptId());
        response.setPartNo(session.getPartNo());
     //   System.out.println(response);

        return response;
    }


}
