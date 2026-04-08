package com.techtricks.Exam_Seating.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SeatRoomResponse {

    private List<Long> roomId;
    private String date;
    private List<Long> sessionId;
}
