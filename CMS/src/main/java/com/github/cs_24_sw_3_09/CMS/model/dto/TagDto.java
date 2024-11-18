package com.github.cs_24_sw_3_09.CMS.model.dto;

import com.github.cs_24_sw_3_09.CMS.model.entities.TagEntity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

public class TagDto {

    private Integer id;
    private String text;


    //No args constructor
    public TagDto() {
    }

    //All args constructor
    public TagDto(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    //Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static class Builder {
        private Integer id;
        private String text;

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public TagDto build() {
            return new TagDto(id, text);
        }
    }

    @Override
    public String toString() {
        return "TagDto [id=" + id + ", text=" + text + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDto that = (TagDto) o;
        return id.equals(that.id) && text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, text);
    }
}
