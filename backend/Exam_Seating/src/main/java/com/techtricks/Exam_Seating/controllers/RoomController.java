package com.techtricks.Exam_Seating.controllers;

import com.techtricks.Exam_Seating.dto.RoomRequest;
import com.techtricks.Exam_Seating.model.Room;
import com.techtricks.Exam_Seating.repository.RoomRepository;
import com.techtricks.Exam_Seating.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomRepository roomRepository;


    @PostMapping
    public ResponseEntity<Room> create(@RequestBody Room room){
        Room saved = roomRepository.save(room);
        roomService.generatedSeatsForRoom(saved.getRoomId());
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Room>> getAll(){
        List<Room> saved = roomRepository.findAll();
        return ResponseEntity.ok(saved);
    }
    @PostMapping("/bulk")
    public ResponseEntity<List<Room>> addRoomsBulk(@RequestBody List<RoomRequest> requests) {
        return ResponseEntity.ok(roomService.addRoomsBulk(requests));
    }


}
