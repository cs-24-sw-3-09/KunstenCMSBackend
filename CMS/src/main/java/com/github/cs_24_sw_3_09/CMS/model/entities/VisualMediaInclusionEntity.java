package com.github.cs_24_sw_3_09.CMS.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "visual_media_inclusion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisualMediaInclusionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "visual_media_inclusion_id_seq")
    @SequenceGenerator(name = "visual_media_inclusion_id_seq", sequenceName = "visual_media_inclusion_id_seq", allocationSize = 1)
    protected Integer id;
    private Integer slideDuration;
    private Integer slideshowPosition;
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "visual_media_id")
    private VisualMediaEntity visualMedia;
}
