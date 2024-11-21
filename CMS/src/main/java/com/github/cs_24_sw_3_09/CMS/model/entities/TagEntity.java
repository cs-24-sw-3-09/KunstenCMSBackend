package com.github.cs_24_sw_3_09.CMS.model.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_id_seq")
    @SequenceGenerator(name = "tag_id_seq", sequenceName = "tag_id_seq", allocationSize = 1)
    private Integer id;
    private String text;
    @ManyToMany(mappedBy = "tags")
    @ToString.Exclude
    @JsonIgnoreProperties(value = {"tags"})
    private Set<VisualMediaEntity> visualMedias = new HashSet<VisualMediaEntity>();

}
