package com.techtricks.coe_auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDto {

    private String username;

    private String email;

    private Long registerNumber;



}
