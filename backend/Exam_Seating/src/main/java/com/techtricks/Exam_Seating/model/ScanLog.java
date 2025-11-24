package com.techtricks.Exam_Seating.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "scan_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScanLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scanId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private SeatAssignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invigilator_id")
    private Invigilator invigilator;


    private String action;
    private String note;

    private String scanTime;
}
