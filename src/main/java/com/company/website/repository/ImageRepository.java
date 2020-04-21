package com.company.website.repository;

import com.company.website.model.Image;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */

public interface ImageRepository extends CrudRepository<Image, Integer> {}
