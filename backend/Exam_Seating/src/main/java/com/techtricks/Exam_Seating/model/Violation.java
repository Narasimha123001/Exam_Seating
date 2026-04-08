package com.techtricks.Exam_Seating.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "violation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Violation {

    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "student_id" , unique = true)
    private Student student;

    private boolean hasViolation;
}
