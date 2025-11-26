package com.techtricks.Exam_Seating.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exam_session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    private String date;       // yyyy-MM-dd
    private String slotCode;   // S1, S2, etc.
    private String startTime;  // HH:mm
    private String endTime;    // HH:mm

    private Integer partNo;    // can be null in JSON if not sent

    // We don't want client to send this on create; we compute later.
    @Builder.Default
    @Column(name = "capacity_required", nullable = false)
    @JsonIgnore           // ignore from JSON in/out if you want; remove if you want it in responses
    private Integer capacityRequired = 0;
}
