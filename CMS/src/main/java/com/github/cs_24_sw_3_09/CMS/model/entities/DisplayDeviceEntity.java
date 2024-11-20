package com.github.cs_24_sw_3_09.CMS.model.entities;

import org.hibernate.annotations.ColumnDefault;
import jakarta.persistence.*;
import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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
    @NotNull
    private String name;
    @NotNull
    private String location;
    @NotNull
    private String model;
    @NotNull
    private String displayOrientation;
    @NotNull
    private String resolution;
    @ColumnDefault("false")
    private Boolean connectedState;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fallback_id")
    private ContentEntity fallbackContent;

}
