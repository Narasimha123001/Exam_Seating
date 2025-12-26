package com.techtricks.Exam_Seating.repository;

import com.techtricks.Exam_Seating.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {

    @Query("SELECT r FROM Room r ORDER BY r.totalCapacity DESC")

    List<Room> findAllByOrderByTotalCapacityDesc();
}
