package com.techtricks.coe_auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffNameDisplay {
    private Long registerNumber;
    private String name;
}
