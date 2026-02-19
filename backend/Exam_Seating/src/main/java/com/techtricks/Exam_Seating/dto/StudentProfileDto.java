package com.techtricks.Exam_Seating.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentProfileDto {
    private Long registerNo;
    private String name;
    private String email;
    private String phoneNo;
    private String departmentName;
    private int year;
    private int semester;
}
