package com.techtricks.Exam_Seating.repository;

import com.techtricks.Exam_Seating.model.Fee;
import com.techtricks.Exam_Seating.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeeRepository extends JpaRepository<Fee, Integer> {

    Optional<Fee> findByStudent(Student student);
}
