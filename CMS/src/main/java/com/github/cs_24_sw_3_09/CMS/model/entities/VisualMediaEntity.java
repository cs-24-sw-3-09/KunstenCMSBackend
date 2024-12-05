package com.github.cs_24_sw_3_09.CMS.model.entities;

import java.util.Set;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "visual_medias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisualMediaEntity extends ContentEntity {

    //@NotNull
    private String name;
    //@NotNull
    private String location;
    //@NotNull
    private String fileType;
    private String description;
    private LocalDateTime lastDateModified;
    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST })
    @JoinTable(name = "visual_media_tag", joinColumns = {
            @JoinColumn(name = "visual_media_id") }, inverseJoinColumns = { @JoinColumn(name = "tag_id") })
    @JsonIgnoreProperties("text")
    @JsonIgnore
    private Set<TagEntity> tags;

    public void addTag(TagEntity tag) {
        tags.add(tag);
    }

    // This updates the lastDateModified incase of the VM being created or updated
    @PrePersist
    @PreUpdate
    public void prePersistOrUpdated() {
        lastDateModified = LocalDateTime.now();
    }
}
