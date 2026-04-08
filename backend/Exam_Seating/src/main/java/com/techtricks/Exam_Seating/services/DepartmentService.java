package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.exception.DepartmentAlreadyPresentException;
import com.techtricks.Exam_Seating.model.Department;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DepartmentService {

    Department saveDepartment(Department department) throws DepartmentAlreadyPresentException;

    Department updateDepartment(String departmentCode , Department newDepartment);

    Department getDepartmentByCode(String code);

    Department getDepartmentByName(String name);

    void deleteDepartmentByCode(String  code);

    List<Department> getAllDepartments();

}
