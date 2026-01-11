package com.techtricks.Exam_Seating.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "subject")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subjectId;

    @Column(nullable = false , unique = true)
    private String subjectCode;

    @Column(nullable = false )
    private String title;


    @ManyToMany
    @JoinTable(
            name = "subject_departments",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "dept_id")
    )
    private List<Department> departments;

    private int semester;

//    @Enumerated(EnumType.STRING)
//    private PaperType paperType;

    private int parts;
    private int year;

//    private int durationMinutes;
}
