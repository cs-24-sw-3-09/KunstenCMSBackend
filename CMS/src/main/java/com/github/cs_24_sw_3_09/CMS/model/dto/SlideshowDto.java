package com.github.cs_24_sw_3_09.CMS.model.dto;

import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlideshowDto extends ContentDto {

    @Size(min = 1, max = 50, message = "a name must be between 1 and 50 characters")
    private String name;
    private Boolean isArchived;
    private Set<VisualMediaInclusionEntity> visualMediaInclusionCollection;

}
