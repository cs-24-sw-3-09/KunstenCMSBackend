package com.github.cs_24_sw_3_09.CMS.model.dto;

import com.github.cs_24_sw_3_09.CMS.model.entities.TagEntity;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisualMediaDto extends ContentDto {

    private String name;
    private String location;
    private String fileType;
    private String description;
    private String lastDateModified;
    private Set<TagEntity> tags;

}
