package com.techtricks.Exam_Seating.controllers;

import com.techtricks.Exam_Seating.exception.DepartmentAlreadyPresentException;
import com.techtricks.Exam_Seating.model.Department;
import com.techtricks.Exam_Seating.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

        private final DepartmentService departmentService;

        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping
        public ResponseEntity<Department> saveDepartment(@RequestBody Department department)
                throws DepartmentAlreadyPresentException {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(departmentService.saveDepartment(department));
        }

        @PutMapping("/{code}")
        public ResponseEntity<Department> updateDepartment(
                @PathVariable String code,
                @RequestBody Department department) {
            return ResponseEntity.ok(departmentService.updateDepartment(code, department));
        }

        @GetMapping("/list")
        public ResponseEntity<List<Department>> getAllDepartments() {
            return ResponseEntity.ok(departmentService.getAllDepartments());
        }

        @GetMapping("/code/{code}")
        public ResponseEntity<Department> getDepartmentByCode(@PathVariable String code) {
            return ResponseEntity.ok(departmentService.getDepartmentByCode(code));
        }

        @GetMapping("/name/{name}")
        public ResponseEntity<Department> getDepartmentByName(@PathVariable String name) {
            return ResponseEntity.ok(departmentService.getDepartmentByName(name));
        }

        @DeleteMapping("/{code}")
        public ResponseEntity<String> deleteDepartmentByCode(@PathVariable String code) {
            departmentService.deleteDepartmentByCode(code);
            return ResponseEntity.ok("Department with code " + code + " deleted successfully");
        }
    }