package com.techtricks.Exam_Seating.repository;

import com.techtricks.Exam_Seating.model.ExamSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamSessionRepository extends JpaRepository<ExamSession,Long> {
    ExamSession findBySessionId(Long sessionId);
    List<ExamSession> findByExam_ExamId(Long examId);


    @Query("SELECT e.capacityRequired FROM ExamSession e WHERE e.sessionId = :sessionId")
    Integer getCapacityBySessionId(@Param("sessionId") Long sessionId);



    @Query("SELECT e FROM ExamSession e " +
            "WHERE e.date = :date AND e.startTime LIKE CONCAT(:time, '%')")
    List<ExamSession> findByDateAndStartTime(
            @Param("date") String date,
            @Param("time") String time);


    @Query("SELECT e FROM ExamSession e WHERE e.slotCode = :slotCode AND e.date = :date")
    List<ExamSession> findByDateAndSlotCode(@Param("date") String date,@Param("slotCode") String slotCode);


}

