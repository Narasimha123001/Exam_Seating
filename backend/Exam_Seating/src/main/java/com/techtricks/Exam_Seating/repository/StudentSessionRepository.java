package com.techtricks.Exam_Seating.repository;

import com.techtricks.Exam_Seating.model.Student;
import com.techtricks.Exam_Seating.model.StudentSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentSessionRepository extends JpaRepository<StudentSession, Long> {
   // List<StudentSession> findBySession_SessionId(Long sessionId);

    @Query("SELECT ss.student FROM StudentSession ss WHERE ss.session = :sessionId")
    List<Student> findStudentsBySession(@Param("sessionId") Long sessionId);


    Long countBySession_SessionId(Long sessionId);
}