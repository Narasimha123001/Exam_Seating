package com.techtricks.Exam_Seating.repository;

import com.techtricks.Exam_Seating.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StudentRepository extends JpaRepository<Student , Long> {

    Optional<Student> findByRegisterNo(Long registerNo);


    Optional<Student> findByEmail(String email);

    @Query("""
        SELECT s FROM Student s
        WHERE (:search IS NULL OR :search = '' OR
               LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(s.email) LIKE LOWER(CONCAT('%', :search, '%')) OR
               CAST(s.registerNo AS string) LIKE CONCAT('%', :search, '%'))
    """)
    Page<Student> searchStudents(@Param("search") String search, Pageable pageable);

    @Query("SELECT s.registerNo FROM Student s WHERE s.year = :year AND s.department.deptId = :deptId")
    List<Long> getRegisterNumbersByYear(int year , Long  deptId);
}
