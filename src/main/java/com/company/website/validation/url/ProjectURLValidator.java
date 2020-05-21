package com.company.website.validation.url;

import com.company.website.dto.ProjectDTO;
import com.company.website.repository.ProjectRepository;

/**
 * Validator for project URL uniqueness
 *
 * @author Dmitry Matrizaev
 * @since 08.05.2020
 */
public class ProjectURLValidator extends CustomURLValidator<ProjectDTO, ProjectRepository> {

    public ProjectURLValidator(ProjectRepository repository) {
        super(repository);
    }

}
