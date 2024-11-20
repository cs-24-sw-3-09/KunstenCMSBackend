package com.github.cs_24_sw_3_09.CMS.model.dto;

import java.sql.Date;
import java.sql.Time;
import java.util.Set;

import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlotDto {

    protected Integer id;
    @Size(min = 1, max = 50, message = "a time slot name must be between 1 and 50 characters")
    private String name;
    private Date startDate;
    private Date endDate;
    private Time startTime;
    private Time endTime;
    @Min(0)
    @Max(127)
    private int weekdaysChosen;
    //private ContentEntity displayContent;
    private Set<DisplayDeviceEntity> displayDevices;

}