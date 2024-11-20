package com.github.cs_24_sw_3_09.CMS.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "visual_medias")
@Data
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
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "visual_media_tag",
            joinColumns = {@JoinColumn(name = "visual_media_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private Set<TagEntity> tags = new HashSet<TagEntity>();


    public VisualMediaEntity addTag(TagEntity tag) {
        tags.add(tag);
        return this;
    }
}
