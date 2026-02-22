package com.techtricks.Exam_Seating.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentListResponse {

    private Long registerNo;
    private String name;
    private String email;



}
