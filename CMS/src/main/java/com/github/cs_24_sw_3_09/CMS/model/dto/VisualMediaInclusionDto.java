package com.github.cs_24_sw_3_09.CMS.model.dto;

import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisualMediaInclusionDto {

    protected Integer id;
    private Integer slideDuration;
    private Integer slideshowPosition;
    private VisualMediaEntity visualMedia;

}