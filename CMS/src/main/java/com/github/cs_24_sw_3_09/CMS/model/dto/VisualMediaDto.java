package com.github.cs_24_sw_3_09.CMS.model.dto;

import jakarta.persistence.*;

public class VisualMediaDto {


    private Integer id;
    private String name;
    private String location;
    private String fileType;
    private String description;
    private String lastDateModified;

    // No-argument constructor
    public VisualMediaDto() {
    }

    // All-arguments constructor
    public VisualMediaDto(Integer id, String name, String location, String fileType,
                             String description, String lastDateModified) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.fileType = fileType;
        this.description = description;
        this.lastDateModified = lastDateModified;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastDateModified() {
        return lastDateModified;
    }

    public void setLastDateModified(String lastDateModified) {
        this.lastDateModified = lastDateModified;
    }

    // Builder Pattern (manual implementation)
    public static class Builder {
        private Integer id;
        private String name;
        private String location;
        private String fileType;
        private String description;
        private String lastDateModified;

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setLocation(String location) {
            this.location = location;
            return this;
        }

        public Builder setFileType(String fileType) {
            this.fileType = fileType;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setLastDateModified(String lastDateModified) {
            this.lastDateModified = lastDateModified;
            return this;
        }

        public VisualMediaDto build() {
            return new VisualMediaDto(id, name, location, fileType, description, lastDateModified);
        }
    }

    @Override
    public String toString() {
        return "VisualMediaEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", fileType='" + fileType + '\'' +
                ", description='" + description + '\'' +
                ", lastDateModified='" + lastDateModified + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisualMediaDto that = (VisualMediaDto) o;
        return id.equals(that.id) && name.equals(that.name) && location.equals(that.location) &&
                fileType.equals(that.fileType) && description.equals(that.description) &&
                lastDateModified.equals(that.lastDateModified);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, location, fileType, description, lastDateModified);
    }

}
