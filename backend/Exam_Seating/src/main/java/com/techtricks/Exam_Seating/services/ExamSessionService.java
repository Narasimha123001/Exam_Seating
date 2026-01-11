package com.techtricks.Exam_Seating.services;


import com.techtricks.Exam_Seating.dto.ExamSessionCreateRequest;
import com.techtricks.Exam_Seating.dto.ExamSessionResponse;
import com.techtricks.Exam_Seating.model.ExamSession;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ExamSessionService {

    public ExamSessionResponse create(ExamSessionCreateRequest session);

    public List<ExamSession> findAll();

    public ExamSession getById(Long sessionId);

    public Long calculateCapacity(Long sessionId);

}
