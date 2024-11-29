package com.github.cs_24_sw_3_09.CMS.model.dto;

import java.sql.Date;
import java.sql.Time;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class TimeSlotDto {

    protected Integer id;
    @Size(min = 1, max = 50, message = "a time slot name must be between 1 and 50 characters")
    private String name;
    @DateTimeFormat(pattern = "yyyy-mm-dd") // If the numbers is not right, it is converted into something valid
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-mm-dd") // If the numbers is not right, it is converted into something valid
    private Date endDate;
    @DateTimeFormat(pattern = "HH:mm") // For time in hours, minutes, seconds
    private Time startTime;
    @DateTimeFormat(pattern = "HH:mm") // For time in hours, minutes, seconds
    private Time endTime;
    @Min(0)
    @Max(127)
    private int weekdaysChosen;
    private ContentEntity displayContent;
    private Set<DisplayDeviceEntity> displayDevices;
}