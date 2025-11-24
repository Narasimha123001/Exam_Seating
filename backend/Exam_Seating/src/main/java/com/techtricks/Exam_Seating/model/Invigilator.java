package com.techtricks.Exam_Seating.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "invigilator")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invigilator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invigilatorId;

    private String name;
    private String phone;
    private String email;
}