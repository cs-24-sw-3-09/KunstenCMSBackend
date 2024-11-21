package com.github.cs_24_sw_3_09.CMS.model.dto;

import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagDto {

    private Integer id;
    private String text;
    private Set<VisualMediaEntity> visualMedias;


}
