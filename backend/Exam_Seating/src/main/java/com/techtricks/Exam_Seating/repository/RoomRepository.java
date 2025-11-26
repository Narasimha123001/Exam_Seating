package com.techtricks.Exam_Seating.repository;

import com.techtricks.Exam_Seating.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
}
