package com.techtricks.Exam_Seating.dto;
import lombok.Data;

import java.util.List;

@Data
public class StudentResponse {
    private String email;
    private List<SubjectResponse> subjects;
}
