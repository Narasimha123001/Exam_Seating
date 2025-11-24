package com.techtricks.Exam_Seating.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;


    @Column(nullable = false , unique = true)
    private Long registerNo;

    @Column(nullable = false )
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Department department;

    private int year;
    private int semester;

    private String email;
    private String phone;
    private String qrCode;

}
