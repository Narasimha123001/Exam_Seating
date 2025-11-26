package com.techtricks.Exam_Seating.repository;

import com.techtricks.Exam_Seating.model.ExamSession;
import com.techtricks.Exam_Seating.model.SeatAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatAssignmentRepository extends JpaRepository<SeatAssignment, Long> {
//
//    List<SeatAssignment> findBySession_SessionId(Long sessionId);
//


    List<SeatAssignment> findBySession_SessionId(Long sessionId);

 //   boolean existsBySession_SessionIdAndSeat_SeatId(Long sessionId, Long seatId);
}

