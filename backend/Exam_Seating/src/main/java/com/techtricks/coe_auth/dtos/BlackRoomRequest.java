package com.techtricks.coe_auth.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BlackRoomRequest {

    private Long blackRoomNumber;
    private String blackRoomName;
}
