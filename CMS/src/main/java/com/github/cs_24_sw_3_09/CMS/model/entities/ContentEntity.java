package com.github.cs_24_sw_3_09.CMS.model.entities;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contents")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class ContentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "content_id_seq")
    @SequenceGenerator(name = "content_id_seq", sequenceName = "content_id_seq", allocationSize = 1)
    protected Integer id;
}
