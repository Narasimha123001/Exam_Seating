package com.techtricks.Exam_Seating.repository;

import com.techtricks.Exam_Seating.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat,Long> {

    @Query("SELECT s FROM Seat s WHERE s.room.roomId IN :roomIds ORDER BY s.rowNo, s.colNo, s.posNo")
    List<Seat> getSeatsForRooms(@Param("roomIds") List<Long> roomIds);
}
