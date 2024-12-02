package com.github.cs_24_sw_3_09.CMS.model.entities;

import java.sql.Date;
import java.sql.Time;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "time_slots")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "time_slot_content")
    private ContentEntity displayContent;

    @NotNull
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "time_slot_display_device", joinColumns = {
            @JoinColumn(name = "time_slot_id")}, inverseJoinColumns = {@JoinColumn(name = "display_device_id")})
    @JsonIgnore
    private Set<DisplayDeviceEntity> displayDevices;

    public void addDisplayDevice(DisplayDeviceEntity displayDevice) {
        this.displayDevices.add(displayDevice);
    }

    public int countDisplayDeviceAssociations() {
        return this.getDisplayDevices().size();
    }

    @Override
public String toString() {
    return "ClassName{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", weekdaysChosen=" + weekdaysChosen +
            ", displayContent=" + (displayContent != null ? displayContent.getId() : null) +
            ", displayDevices=" + (displayDevices != null ? displayDevices.size() : 0) +
            '}';
}

}