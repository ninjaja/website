package com.company.website.repository;

import com.company.website.model.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
public interface ProjectRepository extends CrudRepository<Project, Integer> {

    List<Project> findAllBySubgroupTitle(String subgroupTitle);

    Project findByTitle(String title);

    Project findByUrl(String projectUrl);

    void removeByTitle(String title);

}
