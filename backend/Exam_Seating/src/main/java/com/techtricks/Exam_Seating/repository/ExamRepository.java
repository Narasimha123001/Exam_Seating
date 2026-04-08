package com.techtricks.Exam_Seating.repository;

import com.techtricks.Exam_Seating.model.Exam;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam,Long> {


    @Query(value = "SELECT e FROM Exam e WHERE e.examId = :examId")
    Optional<Exam> findByExamId(@NotNull Long examId);

}
