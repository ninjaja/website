package com.company.website.validation.title;

import com.company.website.dto.ProjectDTO;
import com.company.website.repository.ProjectRepository;

/**
 * @author Dmitry Matrizaev
 * @since 08.05.2020
 */
public class ProjectTitleValidator extends CustomTitleValidator<ProjectDTO, ProjectRepository> {

   public ProjectTitleValidator(ProjectRepository repository) {
      super(repository);
   }

}
