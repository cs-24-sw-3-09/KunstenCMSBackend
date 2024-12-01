package com.github.cs_24_sw_3_09.CMS.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResetNewDto {
    @NotNull
    private String email;
    @NotNull
    private String token;
    @NotNull
    @Size(min = 8, message = "a password must be at least 8 characters long")
    private String password;
}
