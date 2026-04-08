package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.exception.DepartmentAlreadyPresentException;
import com.techtricks.Exam_Seating.exception.DepartmentNotFoundException;
import com.techtricks.Exam_Seating.model.Department;
import com.techtricks.Exam_Seating.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public Department saveDepartment(Department department) throws DepartmentAlreadyPresentException {
        if (departmentExists(department.getCode())) {
            throw new DepartmentAlreadyPresentException(
                    "Department with code " + department.getCode() + " already exists"
            );
        }
        return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(String departmentCode, Department newDepartment) {
        if (!departmentExists(departmentCode)) {
            throw new DepartmentNotFoundException(
                    "Department with code " + departmentCode + " not found"
            );
        }

        Department existingDepartment = findDepartmentByCode(departmentCode);

        if (newDepartment.getName() != null) {
            existingDepartment.setName(newDepartment.getName());
        }
        if (newDepartment.getCode() != null) {
            existingDepartment.setCode(newDepartment.getCode());
        }

        return departmentRepository.save(existingDepartment);
    }

    @Override
    public Department getDepartmentByCode(String code) {
        return departmentRepository.findDepartmentByCode(code)
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department with code " + code + " not found"
                ));
    }

    @Override
    public Department getDepartmentByName(String name) {
        return findDepartmentByName(name);
    }

    @Override
    public void deleteDepartmentByCode(String code) {
        Department department = findDepartmentByCode(code);
        departmentRepository.delete(department);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // ─── Private Helpers ──────────────────────────────────────────────────────

    private boolean departmentExists(String departmentCode) {
        Optional<Department> optionalDepartment = departmentRepository
                .findDepartmentByCode(departmentCode);
        return optionalDepartment.isPresent();
    }

    private Department findDepartmentByCode(String code) {
        Optional<Department> optionalDepartment = departmentRepository
                .findDepartmentByCode(code);
        return optionalDepartment.orElseThrow(() ->
                new DepartmentNotFoundException("Department with code " + code + " not found"));
    }

    private Department findDepartmentByName(String name) {
        Optional<Department> optionalDepartment = departmentRepository
                .findDepartmentByName(name);
        return optionalDepartment.orElseThrow(() ->
                new DepartmentNotFoundException("Department with name " + name + " not found"));
    }
}