package com.company.website.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
@Entity
@Data
@NoArgsConstructor
public class Project implements CustomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String title;

    @NotNull
    private String url;

    private String description;

    @ManyToOne
    @JoinColumn(name = "subgroup_id")
    private Subgroup subgroup;

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
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
