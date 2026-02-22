package com.techtricks.coe_auth.dtos;


import lombok.*;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class BlackRoomUpdateRequest {
    private Long blackRoomId;
    private Long blackRoomNumber;
    private String blackRoomName;
}
