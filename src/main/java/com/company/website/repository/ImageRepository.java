package com.company.website.repository;

import com.company.website.model.Image;
import com.company.website.model.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */

public interface ImageRepository extends CrudRepository<Image, Integer> {

    List<Image> findAllByProject(Project project);

}
