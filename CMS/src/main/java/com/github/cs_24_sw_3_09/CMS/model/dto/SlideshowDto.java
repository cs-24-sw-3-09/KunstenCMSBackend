package com.github.cs_24_sw_3_09.CMS.model.dto;

import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlideshowDto extends ContentDto {

    private String name;
    private Boolean isArchived;
    private Set<VisualMediaInclusionEntity> visualMediaInclusionCollection;

}
