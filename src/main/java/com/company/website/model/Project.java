package com.company.website.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 2, max = 50)
    private String title;

    @NotNull
    @Size(min = 2, max = 50)
    private String url;

    @Size(max = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "subgroup_id")
    private Subgroup subgroup;

    @OneToMany(mappedBy = "project")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Image> images;

    public Project(String title, String url, String description, Subgroup subgroup) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.subgroup = subgroup;
    }

}
