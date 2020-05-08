package com.company.website.repository;

import com.company.website.model.Subgroup;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
public interface SubgroupRepository extends CrudRepository<Subgroup, Integer>, CustomRepository {

    @Override
    Subgroup findByTitle(String title);

    @Override
    boolean existsByTitle(String title);

    @Override
    boolean existsByUrl(String url);

    List<Subgroup> findAllByCategoryTitle(String categoryTitle);

    Subgroup findByUrl(String url);

    void removeByTitle(String title);

    void removeByUrl(String url);
}
