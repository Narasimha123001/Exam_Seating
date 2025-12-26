package com.techtricks.Exam_Seating.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubjectRequest {
    private String subjectCode;
    private String title;
    private List<Long> department;  // Your format
    private int semester;
    private int parts;
    private int year;
}
