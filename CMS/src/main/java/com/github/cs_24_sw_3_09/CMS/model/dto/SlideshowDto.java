package com.github.cs_24_sw_3_09.CMS.model.dto;


import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
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
