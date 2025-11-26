package com.techtricks.Exam_Seating.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="student_session", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id","session_id"}))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class StudentSession {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="student_id", nullable=false)
    private Student student;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="session_id", nullable=false)
    private ExamSession session;

    private String registeredAt;
    @PrePersist public void pre() { if (registeredAt==null) registeredAt = java.time.LocalDateTime.now().toString(); }
}