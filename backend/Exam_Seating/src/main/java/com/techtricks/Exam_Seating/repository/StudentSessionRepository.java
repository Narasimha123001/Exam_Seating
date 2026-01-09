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

    /**
     * Find all student registrations for an exam session
     */
    @Query("SELECT ss FROM StudentSession ss " +
            "WHERE ss.session.sessionId = :sessionId")
    List<StudentSession> findByExamSessionId(@Param("sessionId") Long sessionId);

    /**
     * Get list of students registered for given sessions
     */
    @Query("SELECT DISTINCT ss.student FROM StudentSession ss " +
            "WHERE ss.session.sessionId IN :sessionIds")
    List<Student> findStudentsBySession(@Param("sessionIds") List<Long> sessionIds);

    /**
     * Count students per session
     */
    @Query("SELECT ss.session.sessionId, COUNT(ss) " +
            "FROM StudentSession ss " +
            "WHERE ss.session.sessionId IN :sessionIds " +
            "GROUP BY ss.session.sessionId")
    List<Object[]> countStudentsPerSession(@Param("sessionIds") List<Long> sessionIds);

    Long countBySession_SessionId(Long sessionId);
}