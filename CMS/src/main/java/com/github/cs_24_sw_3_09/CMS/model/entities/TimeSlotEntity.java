package com.github.cs_24_sw_3_09.CMS.model.entities;

import java.sql.Date;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "time_slots")
public class TimeSlotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "time_slot_id_seq")
    @SequenceGenerator(name = "time_slot_id_seq", sequenceName = "time_slot_id_seq", allocationSize = 1)
    protected Integer id;
    @NotNull
    private String name;
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;
    @NotNull
    private Time startTime;
    @NotNull
    private Time endTime;
    @NotNull
    @ColumnDefault("0")
    private int weekdaysChosen;
    // @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "time_slot_content")
    private ContentEntity displayContent;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "time_slot_display_device", joinColumns = {
            @JoinColumn(name = "time_slot_id") }, inverseJoinColumns = { @JoinColumn(name = "display_device_id") })
    @JsonIgnore
    private Set<DisplayDeviceEntity> displayDevices = new HashSet<DisplayDeviceEntity>();

}