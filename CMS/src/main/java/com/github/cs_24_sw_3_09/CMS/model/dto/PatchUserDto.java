package com.github.cs_24_sw_3_09.CMS.model.dto;

import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.*;

public class PatchUserDto extends UserDto {

    @Override
    @JsonProperty(access = JsonProperty.Access.READ_WRITE) // Password is READ_WRITE in the subclass
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }
}