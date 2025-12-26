package com.techtricks.Exam_Seating.dto;

import lombok.Data;

@Data
public class StudentRequest {


    private Long registrationId;
    private String name;
    private Long depId;
    private int year;
    private int sem;

    private String email;
    private String phone;
//    private String qrCode; ->TODO see you next time-> next update

}

//Long regNo, String name, Long deptId, int year, int sem