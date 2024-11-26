package com.github.cs_24_sw_3_09.CMS.model.entities;

import java.util.List;
import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "display_devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
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
    private String displayOrientation;
    @NotNull
    private String resolution;
    @ColumnDefault("false")
    private Boolean connectedState;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "fallback_id")
    private ContentEntity fallbackContent;
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name = "time_slot_display_device", joinColumns = {
            @JoinColumn(name = "display_device_id")}, inverseJoinColumns = {@JoinColumn(name = "time_slot_id")})
    @JsonIgnore
    private List<TimeSlotEntity> timeSlots;

    public void addTimeSlot(TimeSlotEntity timeSlot) {
        this.timeSlots.add(timeSlot);
    }
}
