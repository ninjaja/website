package com.company.website.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Entity bean for subgroups
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
@Entity
@Data
@NoArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Subgroup implements CustomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String title;

    @NotNull
    private String url;

    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Category category;

    @OneToMany(mappedBy = "subgroup")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Project> projects;

    public Subgroup(String title, String url, String description, Category category) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.category = category;
    }

}
