package com.techtricks.Exam_Seating.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exam")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examId;

    private String name;

    @Enumerated(EnumType.STRING)
    private ExamType examType;

    private String startDate;

    private String endDate;

    private String createdBy;


}
