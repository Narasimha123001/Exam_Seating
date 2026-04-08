package com.techtricks.Exam_Seating.controllers;

import com.techtricks.Exam_Seating.dto.RoomRequest;
import com.techtricks.Exam_Seating.exception.RoomAlreadyPresentException;
import com.techtricks.Exam_Seating.model.Room;
import com.techtricks.Exam_Seating.repository.RoomRepository;
import com.techtricks.Exam_Seating.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/examrooms")
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Room>> getAll(){
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Page<Room>> getAllRooms(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String sort){

        return ResponseEntity.ok(roomService.list(page, size, search, sort));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/bulk")
    public ResponseEntity<List<Room>> addRoomsBulk(@RequestBody List<RoomRequest> requests) {
        return ResponseEntity.ok(roomService.addRoomsBulk(requests));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<Room> saveRoom(@RequestBody RoomRequest roomRequest) throws RoomAlreadyPresentException {
        return ResponseEntity.ok(roomService.addRoom(roomRequest));
    }

    @DeleteMapping("/{roomNumber}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long roomNumber) {
        roomService.deleteRoom(roomNumber);
        return ResponseEntity.ok("Room deleted successfully");
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<Room> updateRoom(
            @PathVariable Long roomId,
            @RequestBody RoomRequest request
    ) throws RoomAlreadyPresentException {
        Room updatedRoom = roomService.updateRoom(roomId, request);

        return ResponseEntity.ok(updatedRoom);
    }
}
