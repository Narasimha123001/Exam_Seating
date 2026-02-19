package com.techtricks.coe_auth.controllers;

import com.techtricks.coe_auth.dtos.BlackRoomAccessResponseDto;
import com.techtricks.coe_auth.dtos.BlackRoomAssignDto;
import com.techtricks.coe_auth.exceptions.NoAccessPresentException;
import com.techtricks.coe_auth.exceptions.BlackRoomAccessAlreadyPresentException;
import com.techtricks.coe_auth.exceptions.BlackRoomNotFoundExceptions;
import com.techtricks.coe_auth.exceptions.UserNotFoundException;
import com.techtricks.coe_auth.services.BlackRoomAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/blackRoom")
@RequiredArgsConstructor
public class BlackRoomAccessController {

    private final BlackRoomAccessService blackRoomAccessService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assign")
    public ResponseEntity<BlackRoomAccessResponseDto> assignBlackRoomAccess(@RequestBody BlackRoomAssignDto dto) throws
            BlackRoomNotFoundExceptions, BlackRoomAccessAlreadyPresentException, IllegalAccessException  {
        return ResponseEntity.ok(blackRoomAccessService.assignBlackRoomAccess(dto));
    }

    @GetMapping("/access-list")
    public ResponseEntity<List<BlackRoomAccessResponseDto>> findAllBlackRoomAccess() {
        return ResponseEntity.ok(blackRoomAccessService.findAll());
    }

    @GetMapping("/{blackRoomId}")
    public ResponseEntity<?> findBlackRoomAccess(@PathVariable Long blackRoomId)  {
        System.out.println(blackRoomId);
        try {
            BlackRoomAccessResponseDto findBlackRoomAccess = blackRoomAccessService.findBlackRoomAccess(blackRoomId);
            return ResponseEntity.status(HttpStatus.FOUND).body(findBlackRoomAccess);
            } catch (BlackRoomNotFoundExceptions e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
    }

    @DeleteMapping("/remove/{registerNumber}/{blackRoomName}")
    public ResponseEntity<?> deleteBlackRoomAccess( @PathVariable Long registerNumber , @PathVariable String blackRoomName ) {
        try{
            blackRoomAccessService.removeBlackRoomAccess(registerNumber, blackRoomName);
            return ResponseEntity.ok("Access removed successfully");
        } catch (BlackRoomNotFoundExceptions | NoAccessPresentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/access/{registerNumber}/{blackRoomId}")
    public ResponseEntity<?> validateAccess(@PathVariable Long registerNumber, @PathVariable Long blackRoomId) {
        try{
            boolean validate = blackRoomAccessService.validateAccess(registerNumber, blackRoomId);
            System.out.println(registerNumber +" "+ blackRoomId + " "+ validate);
            if(validate){
                return ResponseEntity.ok("Access validated successfully");
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Access validation failed");
            }
        }catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
