package com.github.cs_24_sw_3_09.CMS.model.dto;

import com.github.cs_24_sw_3_09.CMS.model.entities.TagEntity;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisualMediaDto extends ContentDto {

    @Size(min = 1, max = 50, message = "a name must be between 1 and 50 characters")
    private String name;
    @Size(min = 1, max = 50, message = "a location must be between 1 and 50 characters")
    private String location;
    // TODO: add validation for filetype?
    private String fileType;
    @Size(min = 1, max = 100, message = "a description must be between 1 and 100 characters")
    private String description;
    private LocalDateTime lastDateModified;
    private Set<TagEntity> tags;

}
