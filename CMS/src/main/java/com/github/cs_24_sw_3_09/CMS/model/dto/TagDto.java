package com.github.cs_24_sw_3_09.CMS.model.dto;

import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagDto {
    private Integer id;
    @Size(min = 1, max = 50, message = "a tag text must be between 1 and 50 characters")
    private String text;
    private Set<VisualMediaEntity> visualMedias;
}
