package com.github.cs_24_sw_3_09.CMS.model.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "display_devices")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisplayDeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "display_device_id_seq")
    @SequenceGenerator(name = "display_device_id_seq", sequenceName = "display_device_id_seq", allocationSize = 1)
    protected Integer id;
    private String name;
    private String location;
    private String model;
    private String displayOrientation;
    private String resolution;
    private Boolean connectedState;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "falback_id")
    private VisualMediaEntity fallbackVisualMedia;

}
