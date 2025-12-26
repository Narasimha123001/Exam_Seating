package com.techtricks.Exam_Seating.dto;

import com.techtricks.Exam_Seating.model.Subject;
import lombok.Data;

import java.util.List;

@Data
public class StudentResponse {

    private Long registrationId;
    private String name;
    private int year;
    private int semester;
    private List<SubjectResponse> subjects;
}
