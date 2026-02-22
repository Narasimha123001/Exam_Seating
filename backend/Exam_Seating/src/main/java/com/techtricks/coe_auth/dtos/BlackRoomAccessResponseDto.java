package com.techtricks.coe_auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlackRoomAccessResponseDto {

    private String blackRoomName;
    private String userName;
    private Long registerNumber;

}
