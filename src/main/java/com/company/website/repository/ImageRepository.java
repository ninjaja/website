package com.company.website.repository;

import com.company.website.model.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
public interface ImageRepository extends CrudRepository<Image, Integer> {

    Page<Image> findAllByProjectTitle(String projectTitle, Pageable pageable);

    List<Image> findAllByProjectTitle(String projectTitle);

    Image findByTitle(String title);

    boolean existsByTitle(String title);

    void deleteByTitle(String title);

    Page<Image> findAll(Pageable pageable);

}
