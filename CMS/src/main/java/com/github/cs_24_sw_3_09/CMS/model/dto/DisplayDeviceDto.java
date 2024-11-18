package com.github.cs_24_sw_3_09.CMS.model.dto;


import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisplayDeviceDto {

    protected Integer id;
    private String name;
    private String location;
    private String model;
    private String displayOrientation;
    private String resolution;
    private Boolean connectedState;
    private VisualMediaEntity fallbackVisualMedia;

}