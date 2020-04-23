package com.company.website.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 *
 * @author Dmitry Matrizaev
 * @since 21.04.2020
 */

@Entity
@Table(name = "roles")
@EqualsAndHashCode
@ToString
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int id;

    @NotNull
    @Size(min = 2, max = 10)
    @Getter
    @Setter
    private String name;

    @ManyToMany(mappedBy = "roles")
    @Getter
    @Setter
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<User> users;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

}
