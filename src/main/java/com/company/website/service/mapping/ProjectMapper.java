package com.company.website.service.mapping;

import com.company.website.dto.ProjectDTO;
import com.company.website.model.Project;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Dmitry Matrizaev
 * @since 05.05.2020
 */
@Component
@AllArgsConstructor
public class ProjectMapper {

    private final SubgroupMapper mapper;

    public Project map(ProjectDTO dto) {
        Project project = new Project();
        project.setId(dto.getId());
        project.setTitle(dto.getTitle());
        project.setUrl(dto.getUrl());
        project.setDescription(dto.getDescription());
        return project;
    }

    public ProjectDTO map(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setTitle(project.getTitle());
        dto.setUrl(project.getUrl());
        dto.setDescription(project.getDescription());
        return dto;
    }

    public Project copyFromDto(final ProjectDTO dto, final Project project) {
        project.setTitle(dto.getTitle());
        project.setUrl(dto.getUrl());
        project.setDescription(dto.getDescription());
        return project;
    }

}
