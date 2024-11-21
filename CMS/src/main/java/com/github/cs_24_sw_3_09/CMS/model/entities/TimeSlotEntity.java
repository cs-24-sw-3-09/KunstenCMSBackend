package com.github.cs_24_sw_3_09.CMS.model.entities;

import java.sql.Date;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;

import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "time_slot_content")
    private ContentEntity displayContent;
    
    @NotNull
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name = "time_slot_display_device",
        joinColumns = {@JoinColumn(name = "time_slot_id")},
        inverseJoinColumns = {@JoinColumn(name = "display_device_id")}
    )
    private Set<DisplayDeviceEntity> displayDevices = new HashSet<DisplayDeviceEntity>();

}