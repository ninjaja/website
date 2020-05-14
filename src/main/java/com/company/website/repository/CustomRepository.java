package com.company.website.repository;

import com.company.website.model.CustomEntity;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author Dmitry Matrizaev
 * @since 08.05.2020
 */
@NoRepositoryBean
public interface CustomRepository<T extends CustomEntity> {

    T findByTitle(String title);

    T findByUrl(String url);

    boolean existsByTitle(String title);

    boolean existsByUrl(String url);

}
