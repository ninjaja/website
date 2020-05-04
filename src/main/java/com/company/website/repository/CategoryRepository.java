package com.company.website.repository;

import com.company.website.model.Category;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
public interface CategoryRepository extends CrudRepository<Category, Integer> {

    Category findByTitle(String name);

    Category findByUrl(String url);
}
