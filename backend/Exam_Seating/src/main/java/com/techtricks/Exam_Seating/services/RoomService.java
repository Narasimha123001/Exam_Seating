package com.techtricks.Exam_Seating.services;


import com.techtricks.Exam_Seating.dto.RoomRequest;
import com.techtricks.Exam_Seating.model.Room;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoomService {
    public void generatedSeatsForRoom(Long roomId);
    List<Room> addRoomsBulk(List<RoomRequest> list);
}
