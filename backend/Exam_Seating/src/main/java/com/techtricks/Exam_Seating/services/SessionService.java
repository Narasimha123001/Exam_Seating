package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.model.Exam;
import com.techtricks.Exam_Seating.model.ExamSession;
import org.springframework.stereotype.Service;

@Service
public interface SessionService {

    public ExamSession createSession(Long examId , Long subjectId , String date , String slot);
}
