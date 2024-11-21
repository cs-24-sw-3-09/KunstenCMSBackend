package com.github.cs_24_sw_3_09.CMS.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "visual_media_tag",
            joinColumns = {@JoinColumn(name = "visual_media_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    @JsonIgnoreProperties(value = {"visualMedias"})
    private Set<TagEntity> tags = new HashSet<TagEntity>();


    public void addTag(TagEntity tag) {
        tags.add(tag);
    }
}
