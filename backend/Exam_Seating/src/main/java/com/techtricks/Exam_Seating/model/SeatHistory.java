package com.techtricks.Exam_Seating.model;


import jakarta.persistence.*;
import jdk.jfr.DataAmount;
import lombok.*;

@Entity
@Table(name = "student_seat_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SeatHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private ExamSession examSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="room_id")
    private Room room;

    private int benchIndex;

    private int rowNo;
    private int colNo;
    private int posNo;


}
