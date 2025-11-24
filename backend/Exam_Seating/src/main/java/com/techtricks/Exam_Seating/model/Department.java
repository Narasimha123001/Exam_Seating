package com.techtricks.Exam_Seating.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "department")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deptId;

    @Column(nullable = false , unique = true)
    private String code;

    @Column(nullable = false )
    private String name;

}
