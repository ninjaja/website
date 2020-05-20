package com.company.website.repository;

import com.company.website.model.Category;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.QueryHint;

/**
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
public interface CategoryRepository extends CrudRepository<Category, Integer>, CustomRepository<Category> {

    @Override
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Category findByTitle(String name);

    @Override
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    boolean existsByTitle(String title);

    @Override
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    boolean existsByUrl(String url);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Category findByUrl(String url);

    void removeByTitle(String title);

}
