package com.techtricks.Exam_Seating.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seat")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private  Room room;

    private  int benchIndex;
    private int rowNo;
    private int colNo;
    private int posNo;

    private String seatLabel;
}
