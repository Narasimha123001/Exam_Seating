package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.model.Exam;
import com.techtricks.Exam_Seating.model.ExamType;
import com.techtricks.Exam_Seating.repository.ExamRepository;
import org.springframework.stereotype.Service;

@Service
public class ExamServicesImpl implements ExamService {

    public final ExamRepository examRepository;

    public ExamServicesImpl(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }


    @Override
    public Exam createExam(Exam e) {

        Exam exam =  Exam.builder()
                .name(e.getName())
                .examType(e.getExamType())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .createdBy(e.getCreatedBy())
                .build();
        return examRepository.save(exam);
    }
}
