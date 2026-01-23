package com.techtricks.Exam_Seating.repository;

import com.techtricks.Exam_Seating.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StudentRepository extends JpaRepository<Student , Long> {

    Optional<Student> findByRegisterNo(Long registerNo);


    Optional<Student> findByEmail(String email);
}
