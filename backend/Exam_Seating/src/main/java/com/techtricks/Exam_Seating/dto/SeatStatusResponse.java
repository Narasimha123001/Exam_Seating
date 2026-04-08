package com.techtricks.Exam_Seating.dto;
import com.techtricks.Exam_Seating.model.AssignStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatStatusResponse {

    private Long seatId;
    private AssignStatus status;


}