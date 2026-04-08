package com.techtricks.Exam_Seating.repository;

import com.techtricks.Exam_Seating.model.Student;
import com.techtricks.Exam_Seating.model.Violation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ViolationRepository extends JpaRepository<Violation, Integer> {

    Optional<Violation> findByStudent(Student student);
}
