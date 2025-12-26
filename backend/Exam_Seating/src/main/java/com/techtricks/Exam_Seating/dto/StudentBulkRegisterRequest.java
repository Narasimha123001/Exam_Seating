package com.techtricks.Exam_Seating.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentBulkRegisterRequest {

    private Long sessionId;
    private List<Long> registrationIds;
}
