package com.techtricks.Exam_Seating.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class RoomRequest {
    private String name;
    private int benchesTotal;
    private int seatsPerBench;
    private int totalCapacity;
    private String location;
}
