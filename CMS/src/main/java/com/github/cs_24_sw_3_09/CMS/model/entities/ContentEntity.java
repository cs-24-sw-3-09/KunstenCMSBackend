package com.github.cs_24_sw_3_09.CMS.model.entities;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contents")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = VisualMediaEntity.class, name = "visualMedia"),
        @JsonSubTypes.Type(value = SlideshowEntity.class, name = "slideshow")
})
public abstract class ContentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "content_id_seq")
    @SequenceGenerator(name = "content_id_seq", sequenceName = "content_id_seq", allocationSize = 1)
    protected Integer id;
}
