package com.github.cs_24_sw_3_09.CMS.model.dto;

import java.sql.Time;
import java.util.List;

import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class DisplayDeviceDto {

    protected Integer id;
    @Size(min = 1, max = 50, message = "a name must be between 1 and 50 characters")
    private String name;
    @Size(min = 1, max = 100, message = "a location must be between 1 and 100 characters")
    private String location;
    @Pattern(regexp = "^(vertical|horizontal)$", message = "a display orientationing must be vertical or horizontal")
    private String displayOrientation;
    @Pattern(regexp = "^\\d+x\\d+$", message = "Resolution must be in the format 'widthxheight' (e.g., 1920x1080)")
    private String resolution;
    private ContentEntity fallbackContent;
    private List<TimeSlotEntity> timeSlots;
    private Time monday_start;
    private Time monday_end;
    private Time tuesday_start;
    private Time tuesday_end;
    private Time wednesday_start;
    private Time wednesday_end;
    private Time thursday_start;
    private Time thursday_end;
    private Time friday_start;
    private Time friday_end;
    private Time saturday_start;
    private Time saturday_end;
    private Time sunday_start;
    private Time sunday_end;
}