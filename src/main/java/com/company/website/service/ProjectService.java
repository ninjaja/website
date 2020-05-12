package com.company.website.service;

import com.company.website.dto.CategoryDTO;
import com.company.website.dto.ImageDTO;
import com.company.website.dto.ProjectDTO;
import com.company.website.dto.SubgroupDTO;
import com.company.website.model.Category;
import com.company.website.model.Project;
import com.company.website.model.Subgroup;
import com.company.website.repository.ProjectRepository;
import com.company.website.repository.SubgroupRepository;
import com.company.website.service.mapping.CategoryMapper;
import com.company.website.service.mapping.ProjectMapper;
import com.company.website.service.mapping.SubgroupMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dmitry Matrizaev
 * @since 04.05.2020
 */
@Service
@Transactional
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final SubgroupRepository subgroupRepository;
    private final ImageService imageService;
    private final ProjectMapper projectMapper;
    private final SubgroupMapper subgroupMapper;
    private final CategoryMapper categoryMapper;

    public Iterable<ProjectDTO> findAllBySubgroup(SubgroupDTO subgroup) {
        return projectRepository.findAllBySubgroupTitle(subgroup.getTitle()).stream()
                .map(projectMapper::map)
                .collect(Collectors.toList());
    }

    public void save(ProjectDTO projectDTO, SubgroupDTO subgroupDTO) {
        Project project = projectRepository.findById(projectDTO.getId()).orElseGet(Project::new);
        project = projectMapper.copyFromDto(projectDTO, project);
        project.setSubgroup(subgroupRepository.findByTitle(subgroupDTO.getTitle()));
        projectRepository.save(project);
    }

    public void deleteByTitle(String title) {
        projectRepository.removeByTitle(title);
    }

    public ProjectDTO findByUrl(String url) {
        return projectMapper.map(projectRepository.findByUrl(url));
    }

    public ProjectDTO findById(Integer id) {
        return projectMapper.map(projectRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    public void copyOnError(final ProjectDTO projectDTO) {
        final ProjectDTO oldProjectDTO = findById(projectDTO.getId());
        projectDTO.setTitle(oldProjectDTO.getTitle());
        projectDTO.setUrl(oldProjectDTO.getUrl());
        projectDTO.setDescription(oldProjectDTO.getDescription());
    }

    public Iterable<ProjectDTO> findAllWithImages() {
        List<ProjectDTO> list = new ArrayList<>();
        for (Project project : projectRepository.findAll()) {
            ProjectDTO projectDTO = projectMapper.map(project);
            if (projectDTO.isHasImages()) {
                ImageDTO imageDTO = projectMapper.getAnyImage(project);
                imageDTO.setData(imageService.applyDataOnRead(imageDTO, projectDTO.getTitle()));
                projectDTO.setFirstImage(imageDTO);
                Subgroup subgroup = project.getSubgroup();
                SubgroupDTO subgroupDTO = subgroupMapper.map(subgroup);
                Category category = subgroup.getCategory();
                CategoryDTO categoryDTO = categoryMapper.map(category);
                subgroupDTO.setCategory(categoryDTO);
                projectDTO.setSubgroup(subgroupDTO);
                list.add(projectDTO);
            }
        }
        return list;
    }

}
