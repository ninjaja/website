package com.company.website.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 2, max = 30)
    private String title;

    @NotNull
    @Size(min = 2, max = 30)
    private String url;

    @Size(max = 300)
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
