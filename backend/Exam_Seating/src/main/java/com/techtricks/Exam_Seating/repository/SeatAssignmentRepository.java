package com.techtricks.Exam_Seating.repository;

import com.techtricks.Exam_Seating.dto.SeatStatusResponse;
import com.techtricks.Exam_Seating.model.ExamSession;
import com.techtricks.Exam_Seating.model.SeatAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface SeatAssignmentRepository extends JpaRepository<SeatAssignment, Long> {
//
//    List<SeatAssignment> findBySession_SessionId(Long sessionId);
//


    List<SeatAssignment> findBySession_SessionId(Long sessionId);

 //   boolean existsBySession_SessionIdAndSeat_SeatId(Long sessionId, Long seatId);

    Optional<SeatAssignment> findByStudent_StudentId(Long studentId);



    @Query("""
SELECT DISTINCT sa.room.roomId
FROM SeatAssignment sa
""")
    List<Long> findAllUsedRooms();


//
//
//    @Query("SELECT  s.room.roomId FROM SeatAssignment s WHERE s.session.sessionId = :sessionId")
//    List<Long> findRoomBySession_SessionId(@Param("sessionId") Long sessionId);
//
//    @Query("SELECT s.seat.seatId FROM SeatAssignment s WHERE s.room.roomId =:roomId")
//    List<Long> findSeatByRoom_RoomId(@Param("roomId")Long roomId);
//
//    @Query("SELECT s.student.studentId FROM SeatAssignment s WHERE s.seat.seatId =:seatId")
//    Long findStudentSeat_SeatId(@Param("seatId") Long seatId);

    @Query("""
SELECT sa
FROM SeatAssignment sa
WHERE sa.room.roomId = :roomId
ORDER BY sa.seat.rowNo, sa.seat.colNo
""")
    List<SeatAssignment> findByRoom(@Param("roomId") Long roomId);

    /**
     * Count assignments for given sessions
     */
    @Query("SELECT COUNT(sa) FROM SeatAssignment sa " +
            "WHERE sa.session.sessionId IN :sessionIds")
    long countBySessionIdIn(@Param("sessionIds") List<Long> sessionIds);

    /**
     * Get count per session
     */
    @Query("""
SELECT sa.session.sessionId, COUNT(sa)
FROM SeatAssignment sa
WHERE sa.session.sessionId IN :sessionIds
GROUP BY sa.session.sessionId
""")
    List<Object[]> countPerSession(@Param("sessionIds") List<Long> sessionIds);
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

    @Query("""
    SELECT s.sessionId
    FROM ExamSession s
    WHERE s.date = :date
    AND s.slotCode = :slotCode
""")
    List<Long> getSessionByDateAndSlot(
            @Param("date") LocalDate date,
            @Param("slotCode") String slotCode
    );

    @Query("""
SELECT DISTINCT s.room.roomId
FROM SeatAssignment s
WHERE s.session.date = :date
AND s.session.slotCode = :slotCode
""")
    List<Long> findRoomsByDateAndSlot(
            @Param("date") String date,
            @Param("slotCode") String slotCode
    );

    @Query("""
    SELECT s.sessionId
    FROM ExamSession s
    WHERE s.date = :date
    AND s.slotCode = :slotCode
""")
    List<Long> getSessionByDateAndSlot(
            @Param("date") String date,
            @Param("slotCode") String slotCode
    );





    @Query("""
SELECT DISTINCT s.room.roomId\s
FROM SeatAssignment s\s
WHERE s.session.sessionId = :sessionId
""")
    List<Long> findRoomBySession_SessionId(@Param("sessionId") Long sessionId);



    @Query("""
SELECT s.seat.seatId\s
FROM SeatAssignment s\s
WHERE s.room.roomId = :roomId\s
AND s.session.sessionId = :sessionId
""")
    List<Long> findSeatByRoomAndSession(
            @Param("roomId") Long roomId,
            @Param("sessionId") Long sessionId
    );

    @Query("""
SELECT s.student.studentId\s
FROM SeatAssignment s\s
WHERE s.seat.seatId = :seatId\s
AND s.session.sessionId = :sessionId
""")
    Long findStudentBySeatAndSession(
            @Param("seatId") Long seatId,
            @Param("sessionId") Long sessionId
    );



    ///for attendence

    @Query("""
SELECT s FROM SeatAssignment s
WHERE s.room.roomId = :roomId
AND s.student.studentId = :studentId
AND s.session.sessionId = :sessionId
""")
    Optional<SeatAssignment> findByRoomStudentAndSession(
            @Param("roomId") Long roomId,
            @Param("studentId") Long studentId,
            @Param("sessionId") Long sessionId
    );


    @Query("""
SELECT new com.techtricks.Exam_Seating.dto.SeatStatusResponse(
    s.seat.seatId,
    s.status
)
FROM SeatAssignment s
WHERE s.room.roomId = :roomId
AND s.session.sessionId = :sessionId
""")
    List<SeatStatusResponse> findSeatStatusByRoomAndSession(
            @Param("roomId") Long roomId,
            @Param("sessionId") Long sessionId
    );

}

