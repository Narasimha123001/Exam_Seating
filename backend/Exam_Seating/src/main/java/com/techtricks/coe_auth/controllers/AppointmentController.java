package com.techtricks.coe_auth.controllers;

import com.techtricks.coe_auth.dtos.AppointmentDto;

import com.techtricks.coe_auth.dtos.AppointmentResponseDto;
import com.techtricks.coe_auth.exceptions.AppointmentException;
import com.techtricks.coe_auth.services.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // STUDENT → Book appointment
    @PostMapping("/book")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<AppointmentResponseDto> bookAppointment(
            @RequestBody AppointmentDto appointmentDto,
            @AuthenticationPrincipal UserDetails userDetails)
            throws AppointmentException {

        return ResponseEntity.ok(
                appointmentService.bookAppointment(
                        userDetails.getUsername(),
                        appointmentDto
                )
        );
    }

    // STUDENT → Get only their appointments
    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<AppointmentResponseDto>> getMyAppointments(
            @AuthenticationPrincipal UserDetails userDetails)
            throws AppointmentException {

        return ResponseEntity.ok(
                appointmentService.getAppointmentById(
                        userDetails.getUsername()
                )
        );
    }
    // ADMIN → Get all appointments
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointments() {
        return ResponseEntity.ok(
                appointmentService.getAllAppointments()
        );
    }
}
