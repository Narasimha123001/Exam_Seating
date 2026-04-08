package com.techtricks.Exam_Seating.dto;

import lombok.Data;

@Data
public class ExamSessionResponse {

    private Long sessionId;
    private String name;
    private String subject_title;
    private String subject_code;
    private String date;
    private String slotCode;
    private String startTime;
    private int capacityRequired;
    private int year;
    private Long deptId;
    private String endTime;
    private Integer partNo;
}
