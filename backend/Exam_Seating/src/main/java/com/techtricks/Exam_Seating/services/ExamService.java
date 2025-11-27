package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.model.Exam;
import com.techtricks.Exam_Seating.model.ExamType;
import org.springframework.stereotype.Service;

@Service
public interface ExamService {

    public Exam createExam(Exam exam);
}
