package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.model.Exam;
import com.techtricks.Exam_Seating.model.ExamSession;
import com.techtricks.Exam_Seating.model.Subject;
import com.techtricks.Exam_Seating.repository.ExamRepository;
import com.techtricks.Exam_Seating.repository.ExamSessionRepository;
import com.techtricks.Exam_Seating.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl {

    private final ExamRepository  examRepository;
    private final ExamSessionRepository examSessionRepository;
    private final SubjectRepository subjectRepository;


    public ExamSession createSession(Long examId , Long subjectId , String date , String slot) {

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam Not Found"));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject Not Found"));

        ExamSession examSession = ExamSession.builder()
                .exam(exam)
                .subject(subject)
                .date(date)
                .slotCode(slot)
                .build();
        return examSessionRepository.save(examSession);
    }
}
