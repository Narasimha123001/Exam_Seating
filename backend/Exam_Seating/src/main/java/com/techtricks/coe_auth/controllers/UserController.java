package com.techtricks.coe_auth.controllers;

import com.techtricks.coe_auth.dtos.UserProfileDto;
import com.techtricks.coe_auth.dtos.UserResponseDto;
import com.techtricks.coe_auth.exceptions.UserNotFoundException;
import com.techtricks.coe_auth.models.User;
import com.techtricks.coe_auth.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUser());
    }





    @GetMapping("/regNo/{registerNumber}")
    public ResponseEntity<?> getUserById(@PathVariable Long registerNumber) {
        try{
            User user = userService.getUserById(registerNumber);
            return ResponseEntity.ok(user);
        }catch(UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/regNo/{registerNumber}/update")
    public ResponseEntity<?> updateUser(@PathVariable Long registerNumber , @RequestBody User user) throws UserNotFoundException{
        User updatedUser = userService.getUserById(registerNumber);
        if(updatedUser == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }else{
            return ResponseEntity.ok(userService.updateUser(registerNumber, user));
        }
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/staff/my")
    public ResponseEntity<UserProfileDto> getUSerProfile(){
        String email  = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return ResponseEntity.ok(userService.getUSerByEmail(email));
    }
}
