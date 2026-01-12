package com.techtricks.Exam_Seating.repository;

import com.techtricks.Exam_Seating.model.SeatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface SeatHistoryRepository extends JpaRepository<SeatHistory,Long> {

    @Query("SELECT h FROM SeatHistory h WHERE h.student.studentId = :studentId ORDER BY h.historyId DESC")
    List<SeatHistory> findLastX(@Param("studentId") Long studentId, Pageable pageable);


    @Query("SELECT h FROM SeatHistory h WHERE h.student.studentId = :studentId ORDER BY h.historyId DESC")
    List<SeatHistory> findByStudentIdOrderByHistoryIdDesc(@Param("studentId") Long studentId, Pageable pageable);

    List<SeatHistory> findTop5ByStudent_StudentIdOrderByHistoryIdDesc(Long studentId);

    /**
     * OPTIMIZED: Fetch top 5 histories for multiple students in ONE query
     * This replaces thousands of individual queries
     */
    @Query(value = """

            SELECT * FROM (
            SELECT sh.*,
                   ROW_NUMBER() OVER (
                       PARTITION BY sh.student_id
                       ORDER BY sh.history_id DESC
                   ) as rn
            FROM student_seat_history sh
            WHERE sh.student_id IN :studentIds
        ) sub
        WHERE rn <= 5
        """, nativeQuery = true)
    List<SeatHistory> findTop5PerStudent(@Param("studentIds") List<Long> studentIds);
    }