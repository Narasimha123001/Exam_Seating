package com.techtricks.Exam_Seating.repository;

import com.techtricks.Exam_Seating.model.ExamSession;
import com.techtricks.Exam_Seating.model.SeatAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SeatAssignmentRepository extends JpaRepository<SeatAssignment, Long> {
//
//    List<SeatAssignment> findBySession_SessionId(Long sessionId);
//


    List<SeatAssignment> findBySession_SessionId(Long sessionId);

 //   boolean existsBySession_SessionIdAndSeat_SeatId(Long sessionId, Long seatId);


    /**
     * Count assignments for given sessions
     */
    @Query("SELECT COUNT(sa) FROM SeatAssignment sa " +
            "WHERE sa.session.sessionId IN :sessionIds")
    long countBySessionIdIn(@Param("sessionIds") List<Long> sessionIds);

    /**
     * Get count per session
     */
    @Query("SELECT sa.session.sessionId as sessionId, COUNT(sa) as count " +
            "FROM SeatAssignment sa " +
            "WHERE sa.session.sessionId IN :sessionIds " +
            "GROUP BY sa.session.sessionId")
    Map<Long, Long> countPerSession(@Param("sessionIds") List<Long> sessionIds);

    /**
     * Find all assignments for a session
     */
    @Query("SELECT sa FROM SeatAssignment sa " +
            "WHERE sa.session.sessionId = :sessionId " +
            "ORDER BY sa.seat.room.roomId, sa.seat.rowNo, sa.seat.colNo")
    List<SeatAssignment> findBySessionId(@Param("sessionId") Long sessionId);

        @Query("""
        SELECT sa.room.roomId
        FROM SeatAssignment sa
        WHERE sa.student.studentId = :studentId
        """)
    Long findRoomNoByStudentStudentId(@Param("studentId") Long studentId);

}

