package com.github.cs_24_sw_3_09.CMS.model.dto;

import java.sql.Date;
import java.sql.Time;

import jakarta.validation.constraints.Size;

public class TimeSlotDto {
    
    protected Integer id; 
    @Size(min= 1, max = 50, message = "a time slot name must be between 1 and 50 characters")
    private String name; 
    private Date startDate;
    private Date endDate;
    private Time startTime;
    private Time endTime;
    private int weekdaysChosen; 
    private Integer contentId;

    // No-argument constructor
    public TimeSlotDto(){
    }

    // All-arguments constructor
    private TimeSlotDto(Integer id, String name, Date startDate, Date endDate, Time startTime, Time endTime, int weekdaysChosen, Integer contentId) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.weekdaysChosen = weekdaysChosen;;
        this.contentId = contentId;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public int getWeekdaysChosen() {
        return weekdaysChosen;
    }

    public void setWeekdaysChosen(int weekdaysChosen) {
        this.weekdaysChosen = weekdaysChosen;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    // Builder class
    public static class Builder {
        private Integer id; 
        private String name; 
        private Date startDate;
        private Date endDate;
        private Time startTime;
        private Time endTime;
        private int weekdaysChosen; 
        private Integer contentId;

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setStartDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder setEndDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder setStartTime(Time startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setEndTime(Time endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder setWeekdaysChosen(int weekdaysChosen) {
            this.weekdaysChosen = weekdaysChosen;
            return this;
        }

        public Builder setContentId(Integer contentId) {
            this.contentId = contentId;
            return this;
        }

        public TimeSlotDto build() {
            return new TimeSlotDto(id, name, startDate, endDate, startTime, endTime, weekdaysChosen, contentId);
        }
    }

    @Override
    public String toString() {
        return "TimeSlotDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", weekdaysChosen=" + weekdaysChosen +
                ", contentId=" + contentId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TimeSlotDto that = (TimeSlotDto) o;
        return id.equals(that.id) && name.equals(that.name) && startDate.equals(that.startDate) &&
                endDate.equals(that.endDate) && startTime.equals(that.startTime) &&
                endTime.equals(that.endTime) && weekdaysChosen == that.weekdaysChosen &&
                contentId.equals(that.contentId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, startDate, endDate, startTime, endTime, weekdaysChosen,
                contentId);
    }
    
}