package com.techtricks.Exam_Seating.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatAssignmentResponse {

    private Long assignmentId;
    private Long studentId;
    private Long roomId;
    private Long seatId;
    private int rowNo;
    private int colNo;
}