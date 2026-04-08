package com.techtricks.Exam_Seating.repository;

import com.techtricks.Exam_Seating.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DepartmentRepository extends JpaRepository<Department , Long> {


    Optional<Department> findDepartmentByCode(String code);

    Optional<Department> findDepartmentByDeptId(Long deptId);

    Optional<Department> findDepartmentByName(String name);

    List<Department> findByNameIn(List<String> names);

    List<Department> findAllByDeptIdIn(List<Long> ids);
}
