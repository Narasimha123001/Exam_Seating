package com.techtricks.coe_auth.controllers;

import com.techtricks.coe_auth.dtos.BlackRoomRequest;
import com.techtricks.coe_auth.dtos.BlackRoomResponse;
import com.techtricks.coe_auth.dtos.BlackRoomUpdateRequest;
import com.techtricks.coe_auth.exceptions.BlackRoomAlreadyPresentException;
import com.techtricks.coe_auth.exceptions.BlackRoomNotFoundExceptions;
import com.techtricks.coe_auth.services.BlackRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blackRoom")
@RequiredArgsConstructor
public class BlackCoeRoomController {

    private final BlackRoomService blackRoomService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add/room")
    public ResponseEntity<BlackRoomResponse> addBlackRoom(@RequestBody BlackRoomRequest request) throws BlackRoomAlreadyPresentException {
        return ResponseEntity.ok(blackRoomService.addBlackRoom(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/room")
    public ResponseEntity<BlackRoomResponse> updateBlackRoom(@RequestBody BlackRoomUpdateRequest request) throws BlackRoomNotFoundExceptions {
        return ResponseEntity.ok(blackRoomService.updateBlackRoom(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<BlackRoomResponse>> getAllBlackRooms() throws BlackRoomNotFoundExceptions {
        return ResponseEntity.ok(blackRoomService.getAllBlackRooms());
    }
}
