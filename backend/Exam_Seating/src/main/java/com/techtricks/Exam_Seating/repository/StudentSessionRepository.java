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

    @Query("SELECT ss.student FROM StudentSession ss WHERE ss.session.sessionId = :sessionIds")
    List<Student> findStudentsBySession(@Param("sessionIds") List<Long> sessionIds);


//    @Query("SELECT ss.student FROM StudentSession ss WHERE ss.session.sessionId IN :sessionIds")
//    List<StudentSession> findStudentsBySessionIds(List<Long> sessionIds);
    List<StudentSession> findBySessionSessionId(Long sessionId);
    Long countBySession_SessionId(Long sessionId);


    @Query("SELECT ss.session.sessionId FROM StudentSession ss " +
            "WHERE ss.student.studentId = :studentId AND ss.session.sessionId IN :sessionIds")
    Long findSessionIdForStudent(Long studentId, List<Long> sessionIds);


    // ---- new efficient pair query ----
    // returns rows of [ studentId, subjectId ] for all students in the provided sessions
    @Query("SELECT ss.student.studentId, ss.session.subject.subjectId FROM StudentSession ss " +
            "WHERE ss.session.sessionId IN :sessionIds")
    List<Object[]> findStudentIdAndSubjectIdForSessionIds(@Param("sessionIds") List<Long> sessionIds);

    @Query("SELECT ss.student FROM StudentSession ss WHERE ss.session.sessionId IN :sessionIds")
    List<Student> findStudentsBySessionIds(@Param("sessionIds") List<Long> sessionIds);


    //new one
    @Query("SELECT ss FROM StudentSession ss WHERE ss.session.sessionId = :sessionId")
    List<StudentSession> findByExamSessionId(@Param("sessionId") Long sessionId);
}
