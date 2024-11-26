package com.github.cs_24_sw_3_09.CMS.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "slideshows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlideshowEntity extends ContentEntity {

    private String name;
    private Boolean isArchived;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "slideshow_id")
    private Set<VisualMediaInclusionEntity> visualMediaInclusionCollection;

    public void addVisualMediaInclusion(VisualMediaInclusionEntity visualMediaInclusionEntity) {
        this.visualMediaInclusionCollection.add(visualMediaInclusionEntity);
    }
}
