package com.techtricks.Exam_Seating.dto;

import lombok.Data;

@Data
public class ExamSessionCreateRequest {

    private Long examId;
    private Long subjectId;
    private String date;
    private String slotCode;
    private String startTime;
    private String endTime;
    private Integer partNo;   // optional; can be null
}
