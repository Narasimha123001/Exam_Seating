package com.techtricks.Exam_Seating.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentRegisterResponse {

    private Long sessionId;
    private List<Long> registrationNo;
    private List<Long> failed;
}
