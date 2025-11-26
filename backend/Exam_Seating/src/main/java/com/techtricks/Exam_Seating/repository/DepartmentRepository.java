package com.techtricks.Exam_Seating.repository;

import com.techtricks.Exam_Seating.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DepartmentRepository extends JpaRepository<Department , Long> {
   
}
