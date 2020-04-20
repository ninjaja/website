package com.company.website.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String url;

    private String description;

    @OneToMany(mappedBy = "category")
    private Set<Subgroup> subgroups;

    public Category(String title, String url, String description) {
        this.title = title;
        this.url = url;
        this.description = description;
    }

    public Category() {
    }
}
