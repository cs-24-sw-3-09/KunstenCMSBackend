package com.github.cs_24_sw_3_09.CMS.model.entities;

import java.sql.Date;
import java.sql.Time;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    public boolean overlaps(TimeSlotEntity ts) {
        //Check if any weekdays overlap
        if ((this.weekdaysChosen & ts.weekdaysChosen) == 0) return false; 
        //Check if dates overlaps
        if (this.endDate.before(ts.startDate) || this.startDate.after(ts.endDate)) return false;
        //Check if time overlaps
        if (this.endTime.before(ts.startTime) || this.endTime.equals(ts.startTime)
        //Check if time slots begin right after each other
        || this.startTime.equals(ts.endTime) || this.startTime.after(ts.endTime)) return false;
        
        //Time Slots overlap
        return true;


        //Check if dates overlaps
        /*if (!(this.startDate.before(ts.endDate) && this.endDate.after(ts.startDate)
        || this.startDate.after(ts.startDate) && this.endDate.before(ts.endDate)
        || this.startDate.before(ts.startDate) && this.endDate.after(ts.endDate)
        || this.startDate.equals(ts.startDate) && this.endDate.equals(ts.endDate)
        || this.startDate.before(ts.startDate) && this.endDate.equals(ts.endDate)
        || this.startDate.after(ts.startDate) && this.endDate.equals(ts.endDate)
        || this.startDate.equals(ts.startDate) && this.endDate.after(ts.endDate)
        || this.startDate.equals(ts.startDate) && this.endDate.before(ts.endDate)
        )) {
            return false; 
        }*/

        //Check if time overlaps
        /*if (!(this.startTime.before(ts.endTime) && this.endTime.after(ts.startTime)
        || this.startTime.after(ts.startTime) && this.endTime.before(ts.endTime)
        || this.startTime.before(ts.startTime) && this.endTime.after(ts.endTime)
        || this.startTime.equals(ts.startTime) && this.endTime.equals(ts.endTime)
        || this.startTime.before(ts.startTime) && this.endTime.equals(ts.endTime)
        || this.startTime.after(ts.startTime) && this.endTime.equals(ts.endTime)
        || this.startTime.equals(ts.startTime) && this.endTime.after(ts.endTime)
        || this.startTime.equals(ts.startTime) && this.endTime.before(ts.endTime)
        )) {
            return false; 
        }*/
        
    }

    public boolean overlapsWrong(TimeSlotEntity timeSlotEntity) {

        if (this.startDate.after(timeSlotEntity.startDate) && this.endDate.before(timeSlotEntity.endDate)) {
            return true;
        }

        if (this.startDate.before(timeSlotEntity.startDate) && this.endDate.after(timeSlotEntity.endDate)) {
            return true;
        }

        if (this.startDate.before(timeSlotEntity.startDate) && this.endDate.equals(timeSlotEntity.endDate)) {
            return this.endTime.after(timeSlotEntity.endTime);
        }

        if (this.startDate.after(timeSlotEntity.startDate) && this.endDate.equals(timeSlotEntity.endDate)) {
            return this.endTime.before(timeSlotEntity.endTime);
        }
        if (this.startDate.equals(timeSlotEntity.startDate) && this.endDate.after(timeSlotEntity.endDate)) {
            return this.startTime.before(timeSlotEntity.endTime);
        }

        if (this.startDate.equals(timeSlotEntity.startDate) && this.endDate.before(timeSlotEntity.endDate)) {
            return this.endTime.after(timeSlotEntity.startTime);
        }

        if (this.startDate.equals(timeSlotEntity.startDate) && this.endDate.equals(timeSlotEntity.endDate)) {
            return this.startTime.before(timeSlotEntity.endTime) && this.endTime.after(timeSlotEntity.startTime);
        }

        return false;
    }
}