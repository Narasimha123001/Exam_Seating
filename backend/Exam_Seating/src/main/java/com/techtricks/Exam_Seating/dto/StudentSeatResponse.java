package com.techtricks.Exam_Seating.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentSeatResponse {
    private Long studentId;
    private Long roomId;
    private Long seatId;
}