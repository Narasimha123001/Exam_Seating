package com.techtricks.Exam_Seating.controllers;

import com.techtricks.Exam_Seating.dto.*;
import com.techtricks.Exam_Seating.services.SeatAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seats")
@RequiredArgsConstructor
public class SeatAssignmentController {


    private final SeatAssignmentService service;

    @GetMapping("/rooms")
    public ResponseEntity<SeatRoomResponse> getRoomsByDateAndSlot(
            @RequestParam String date,
            @RequestParam String slotCode) {



        SeatRoomResponse response =
                service.getRoomsByDateAndSlot(date, slotCode);

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/seats")
//    public ResponseEntity<List<Long>> getSeatsByRoom(
//            @RequestParam Long roomId,
//            @RequestParam Long sessionId) {
//
//        List<Long> seats = service.getSeatsByRoom(roomId, sessionId);
//        return ResponseEntity.ok(seats);
//    }

    @GetMapping("/seats")
    public ResponseEntity<List<SeatStatusResponse>> getSeatsByRoom(
            @RequestParam Long roomId,
            @RequestParam Long sessionId) {

        return ResponseEntity.ok(service.getSeatsByRoom(roomId, sessionId));
    }

    @GetMapping("/student")
    public ResponseEntity<Long> getStudentBySeat(
            @RequestParam Long seatId,
            @RequestParam Long sessionId) {


        Long studentId = service.getStudentBySeat(seatId, sessionId);
        return ResponseEntity.ok(studentId);
    }
}