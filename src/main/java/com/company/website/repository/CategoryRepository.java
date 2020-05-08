package com.company.website.repository;

import com.company.website.model.Category;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
public interface CategoryRepository extends CrudRepository<Category, Integer>, CustomRepository<Category> {

    @Override
    Category findByTitle(String name);

    @Override
    boolean existsByTitle(String title);

    @Override
    boolean existsByUrl(String url);

    Category findByUrl(String url);

    void removeByTitle(String title);

}
