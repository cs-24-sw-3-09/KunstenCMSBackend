package com.github.cs_24_sw_3_09.CMS.modelClasses;

import java.sql.Date;
import java.sql.Time;

public class TimeSlot {

    public static class TimeSlotBuilder {
        private TimeSlot ts;

        public TimeSlotBuilder() {
            ts = new TimeSlot();
        }

        public void setId(int id) {
            ts.id = id;
        }

        public void setName(String name) {
            ts.setName(name);
        }

        public void setStartDate(Date startDate) {
            ts.setStartDate(startDate);
        }

        public void setEndDate(Date endDate) {
            ts.setEndDate(endDate);
        }

        public void setStartTime(Time startTime) {
            ts.setStartTime(startTime);
        }

        public void setEndTime(Time endTime) {
            ts.setEndTime(endTime);
        }

        // TODO: import weekdays
        public void setWeekdaysChosenId(int weekdaysChosenId) {
            ts.setWeekdaysChosenId(weekdaysChosenId);
        }

        public void setPriorityRank(int priorityRank) {
            ts.setPriorityRank(priorityRank);
        }

        public void setContentId(int contentId) {
            ts.setContentId(contentId);
        }

        public TimeSlot getTimeSlot() {
            // If id == -1 make a new DD in the DB or do nothing
            return ts;
        }

    }

    protected int id = -1;
    private String name;
    private Date startDate;
    private Date endDate;
    private Time startTime;
    private Time endTime;
    private int weekdaysChosenId;
    private int priorityRank;
    private int contentId;

    public int getId() {
        return id;
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

    public int getWeekdaysChosenId() {
        return weekdaysChosenId;
    }

    public void setWeekdaysChosenId(int weekdaysChosenId) {
        this.weekdaysChosenId = weekdaysChosenId;
    }

    public int getPriorityRank() {
        return priorityRank;
    }

    public void setPriorityRank(int priorityRank) {
        this.priorityRank = priorityRank;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }
}
