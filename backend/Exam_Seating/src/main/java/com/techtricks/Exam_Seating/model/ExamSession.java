package com.techtricks.Exam_Seating.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exam_session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long SessionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exam_id")
    private Exam exam;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    private  Subject subject;

    private String date;

    private String slotCode;

    private String startTime;

    private String endTime;

    private int partNo;

    private int capacityRequired;
}
