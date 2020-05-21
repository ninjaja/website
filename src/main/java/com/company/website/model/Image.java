package com.company.website.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity bean for images
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
@Entity
@Data
@NoArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 2, max = 30)
    private String title;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Project project;

    public Image(String title, Project project) {
        this.title = title;
        this.project = project;
    }

}
