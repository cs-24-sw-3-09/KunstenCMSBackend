package com.github.cs_24_sw_3_09.CMS.model.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Data
@Getter
@Setter
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
    private Set<VisualMediaEntity> visualMedias = new HashSet<VisualMediaEntity>();

}
