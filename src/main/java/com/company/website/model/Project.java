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
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int id;

    @NotNull
    @Size(min = 2, max = 30)
    @Getter
    @Setter
    private String title;

    @NotNull
    @Size(min = 2, max = 30)
    @Getter
    @Setter
    private String url;

    @Size(max = 300)
    @Getter
    @Setter
    private String description;

    @ManyToOne
    @JoinColumn(name = "subgroup_id")
    @Getter
    @Setter
    private Subgroup subgroup;

    @OneToMany(mappedBy = "project")
    @Getter
    @Setter
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Image> images;

    public Project() {
    }

    public Project(String title, String url, String description, Subgroup subgroup) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.subgroup = subgroup;
    }
}
