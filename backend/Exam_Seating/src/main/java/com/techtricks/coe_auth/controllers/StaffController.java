package com.techtricks.coe_auth.controllers;

import com.techtricks.coe_auth.dtos.StaffNameDisplay;
import com.techtricks.coe_auth.exceptions.StaffNotPresentException;
import com.techtricks.coe_auth.services.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StaffNameDisplay>> getAllStaff() throws StaffNotPresentException {
        return ResponseEntity.ok(staffService.getStaffNames());
    }
}
