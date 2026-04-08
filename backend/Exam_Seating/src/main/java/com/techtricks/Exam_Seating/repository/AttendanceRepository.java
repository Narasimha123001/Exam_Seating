package com.techtricks.Exam_Seating.repository;

import com.techtricks.Exam_Seating.model.Attendance;
import com.techtricks.Exam_Seating.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    Optional<Attendance> findByStudent(Student student);
}
