package com.techtricks.Exam_Seating.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StudentExamRoomDetails {

    private List<Long> seatId;

//    private String name;
}
