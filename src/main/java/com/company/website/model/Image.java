package com.company.website.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String title;

    @Lob
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public Image(String title, byte[] data, Project project) {
        this.title = title;
        this.data = data;
        this.project = project;
    }

    public Image() {
    }
}
