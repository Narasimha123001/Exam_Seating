package com.techtricks.Exam_Seating.model;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "student_id", unique = true)
    private Student student;

    private double percentage;
}