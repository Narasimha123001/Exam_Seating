package com.techtricks.Exam_Seating.repository;

import com.techtricks.Exam_Seating.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository  extends JpaRepository<Subject, Long> {
    @Query("SELECT s FROM Subject s JOIN s.departments d WHERE d.deptId = :deptId AND s.semester = :sem AND s.year = :year")
    List<Subject> findByDeptSemYear(@Param("deptId") Long deptId,
                                    @Param("sem") int sem,
                                    @Param("year") int year);

}
