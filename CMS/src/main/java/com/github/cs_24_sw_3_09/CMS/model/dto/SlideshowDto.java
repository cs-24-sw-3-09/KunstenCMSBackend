package com.github.cs_24_sw_3_09.CMS.model.dto;


import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import jakarta.persistence.*;

import java.util.Set;

public class SlideshowDto {
    protected Integer id;
    private String name;
    private Boolean isArchived;
    private Set<VisualMediaInclusionEntity> visualMediaInclusionCollection;

    public SlideshowDto(Integer id, String name, boolean isArchived, Set<VisualMediaInclusionEntity> visualMediaInclusionCollection) {
        this.id = id;
        this.name = name;
        this.isArchived = isArchived;
        this.visualMediaInclusionCollection = visualMediaInclusionCollection;
    }

    public SlideshowDto() {}

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

    public Boolean isArchived() {
        return isArchived;
    }

    public void setArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }

    public Set<VisualMediaInclusionEntity> getVisualMediaInclusionCollection() {
        return visualMediaInclusionCollection;
    }

    public void setVisualMediaInclusionCollection(Set<VisualMediaInclusionEntity> visualMediaInclusionCollection) {
        this.visualMediaInclusionCollection = visualMediaInclusionCollection;
    }

    @Override
    public String toString() {
        return "SlideshowDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isArchived=" + isArchived +
                ", visualMediaInclusionCollection=" + visualMediaInclusionCollection +
                '}';
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, isArchived, visualMediaInclusionCollection);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlideshowDto that = (SlideshowDto) o;
        return id.equals(that.id) && name.equals(that.name) && isArchived == that.isArchived
                && java.util.Objects.equals(visualMediaInclusionCollection, that.visualMediaInclusionCollection);
    }

    public static class Builder {
        private Integer id;
        private String name;
        private Boolean isArchived;
        private Set<VisualMediaInclusionEntity> visualMediaInclusionCollection;


        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setIsArchived(Boolean isArchived) {
            this.isArchived = isArchived;
            return this;
        }

        public Builder setVisualMediaInclusionCollection(Set<VisualMediaInclusionEntity> visualMediaInclusionCollection) {
            this.visualMediaInclusionCollection = visualMediaInclusionCollection;
            return this;
        }

        public SlideshowDto build() {
            return new SlideshowDto(id, name, isArchived, visualMediaInclusionCollection );
        }
    }

}
