package com.github.cs_24_sw_3_09.CMS.model.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
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
    @Size(min = 1, max = 50, message = "a model must be between 1 and 50 characters")
    private String model;
    @Pattern(regexp = "^(vertical|horizontal)$", message = "a display orientationing must be vertical or horizontal")
    private String displayOrientation;
    @Pattern(regexp = "^\\d+x\\d+$", message = "Resolution must be in the format 'widthxheight' (e.g., 1920x1080)")
    private String resolution;
    private Boolean connectedState;
    private ContentEntity fallbackContent;
    private List<TimeSlotEntity> timeSlots;

}