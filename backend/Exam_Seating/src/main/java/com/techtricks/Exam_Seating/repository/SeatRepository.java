package com.techtricks.Exam_Seating.repository;

import com.techtricks.Exam_Seating.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat,Long> {

    List<Seat> findByRoom_RoomIdInOrderByRoom_RoomIdAscBenchIndexAscPosNoAsc(
            List<Long> roomIds
    );

    @Query("SELECT s FROM Seat s WHERE s.room.roomId IN :roomIds ORDER BY s.rowNo, s.colNo, s.posNo")
    List<Seat> getSeatsForRooms(@Param("roomIds") List<Long> roomIds);

    /**
     * Count total seats in rooms
     */
    @Query("SELECT COUNT(s) FROM Seat s WHERE s.room.roomId IN :roomIds")
    long countByRoomIdIn(@Param("roomIds") List<Long> roomIds);
}
