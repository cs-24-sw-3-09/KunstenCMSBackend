package com.github.cs_24_sw_3_09.CMS.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "visual_medias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisualMediaEntity extends ContentEntity {

    private String name;
    private String location;
    private String fileType;
    private String description;
    private String lastDateModified;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "visual_media_tag",
            joinColumns = {@JoinColumn(name = "visual_media_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    @JsonIgnoreProperties("text")
    @JsonIgnore

    private Set<TagEntity> tags;


    public void addTag(TagEntity tag) {
        tags.add(tag);
    }
}
