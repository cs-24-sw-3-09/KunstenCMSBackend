package com.github.cs_24_sw_3_09.CMS.model.entities;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_id_seq")
    @SequenceGenerator(name = "tag_id_seq", sequenceName = "tag_id_seq", allocationSize = 1)
    private Integer id;
    private String text;

    @ManyToMany(mappedBy = "tags")
    private Set<VisualMediaEntity> visualMedias = new HashSet<VisualMediaEntity>();


    //No args constructor
    public TagEntity() {
    }

    //All args constructor
    public TagEntity(Integer id, String text) {
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

        public TagEntity build() {
            return new TagEntity(id, text);
        }
    }

    @Override
    public String toString() {
        return "TagEntity [id=" + id + ", text=" + text + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagEntity that = (TagEntity) o;
        return id.equals(that.id) && text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, text);
    }

}
