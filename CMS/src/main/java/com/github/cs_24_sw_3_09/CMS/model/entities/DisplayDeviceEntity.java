package com.github.cs_24_sw_3_09.CMS.model.entities;

import java.sql.Time;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "fallback_id")
    private ContentEntity fallbackContent;
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name = "time_slot_display_device", joinColumns = {
            @JoinColumn(name = "display_device_id")}, inverseJoinColumns = {@JoinColumn(name = "time_slot_id")})
    @JsonIgnore
    private List<TimeSlotEntity> timeSlots;

    private Time monday_start;
    private Time monday_end;
    private Time tuesday_start;
    private Time tuesday_end;
    private Time wednesday_start;
    private Time wednesday_end;
    private Time thursday_start;
    private Time thursday_end;
    private Time friday_start;
    private Time friday_end;
    private Time saturday_start;
    private Time saturday_end;
    private Time sunday_start;
    private Time sunday_end;

    public void addTimeSlot(TimeSlotEntity timeSlot) {
        this.timeSlots.add(timeSlot);
    }

    public String toStringWithoutTSAndFallback() {
        StringBuilder sb = new StringBuilder();

        if (id != null)
            sb.append("Id: ").append(id).append("<br>");
        if (name != null)
            sb.append("Name: ").append(name).append("<br>");
        if (location != null)
            sb.append("Location: ").append(location).append("<br>");

        return sb.toString()/*.trim()*/;
    }

}
