package com.techtricks.coe_auth.dtos;
import com.techtricks.coe_auth.models.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    private String name;
    @NotBlank
    private String email;
    @NotNull
    private Long registerNumber;
    @NotNull
    private Role role;
}
