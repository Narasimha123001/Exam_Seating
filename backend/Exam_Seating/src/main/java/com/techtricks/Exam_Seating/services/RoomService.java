package com.techtricks.Exam_Seating.services;


import com.techtricks.Exam_Seating.dto.RoomRequest;
import com.techtricks.Exam_Seating.exception.RoomAlreadyPresentException;
import com.techtricks.Exam_Seating.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public interface RoomService {

    Room updateRoom(Long roomNumber , RoomRequest roomRequest) throws RoomAlreadyPresentException;

    Room addRoom(RoomRequest roomRequest) throws RoomAlreadyPresentException;

    public void generatedSeatsForRoom(Long roomId);
    List<Room> addRoomsBulk(List<RoomRequest> list);

    public List<Long> selectRoomsForTotalStudents(int totalStudents);

    List<Room> getAllRooms();


    void deleteRoom(Long roomNumber);


    Page<Room> list(
            int page ,
            int size ,
            String search,
            String sort
    );
}
