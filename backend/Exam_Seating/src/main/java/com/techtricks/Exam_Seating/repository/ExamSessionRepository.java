package com.techtricks.Exam_Seating.repository;

import com.techtricks.Exam_Seating.model.ExamSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamSessionRepository extends JpaRepository<ExamSession,Long> {
    List<ExamSession> findByExam_ExamId(Long examId);
}
