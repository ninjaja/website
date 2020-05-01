package com.company.website.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@EqualsAndHashCode
@ToString
public class Subgroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int id;

    @NotNull
    @Size(min = 2, max = 50)
    @Getter
    @Setter
    private String title;

    @NotNull
    @Size(min = 2, max = 50)
    @Getter
    @Setter
    private String url;

    @Size(max = 1000)
    @Getter
    @Setter
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @Getter
    @Setter
    private Category category;

    @OneToMany(mappedBy = "subgroup")
    @Getter
    @Setter
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Project> projects;

    public Subgroup(String title, String url, String description, Category category) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.category = category;
    }

    public Subgroup() {
    }
}
