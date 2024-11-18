package com.github.cs_24_sw_3_09.CMS.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "visual_media_inclusion")
public class VisualMediaInclusionEntity {
    @Id
    private Integer id;


}
