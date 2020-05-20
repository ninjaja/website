package com.company.website.service;

import com.company.website.dto.ImageDTO;
import com.company.website.dto.ProjectDTO;
import com.company.website.model.Project;
import com.company.website.repository.ProjectRepository;
import com.company.website.repository.SubgroupRepository;
import com.company.website.service.mapping.ProjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Dmitry Matrizaev
 * @since 04.05.2020
 */
@Service
@Transactional
@AllArgsConstructor
public class ProjectService {

    private final SubgroupRepository subgroupRepository;
    private final ProjectRepository projectRepository;
    private final ImageService imageService;
    private final ProjectMapper projectMapper;

    public List<ProjectDTO> findAllBySubgroupUrl(final String subgroupUrl) {
        return projectRepository.findAllBySubgroupUrl(subgroupUrl).stream()
                .map(projectMapper::map)
                .collect(Collectors.toList());
    }

    public void save(final ProjectDTO projectDTO, final String subgroupUrl) {
        Project project = projectRepository.findById(projectDTO.getId()).orElseGet(Project::new);
        project = projectMapper.copyFromDto(projectDTO, project);
        project.setSubgroup(subgroupRepository.findByUrl(subgroupUrl));
        projectRepository.save(project);
    }

    public void save(final ProjectDTO projectDTO, final String subgroupUrl, final MultipartFile[] files) {
        save(projectDTO, subgroupUrl);
        projectDTO.setId(projectRepository.findByTitle(projectDTO.getTitle()).getId());
        imageService.processImagesOnWrite(files, projectDTO);
    }

    public void deleteByTitle(final String title) {
        projectRepository.removeByTitle(title);
    }

    public ProjectDTO findByUrl(final String url) {
        return projectMapper.map(projectRepository.findByUrl(url));
    }

    public ProjectDTO findById(final Integer id) {
        return projectMapper.map(projectRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    public void copyDTOValuesOnEditError(final ProjectDTO projectDTO) {
        final ProjectDTO oldProjectDTO = findById(projectDTO.getId());
        projectDTO.setTitle(oldProjectDTO.getTitle());
        projectDTO.setUrl(oldProjectDTO.getUrl());
        projectDTO.setDescription(oldProjectDTO.getDescription());
    }

    public Iterable<ProjectDTO> findAllWithImages() {
        List<ProjectDTO> list = new ArrayList<>();
        projectRepository.findAll().forEach(project -> populateImages(list, project));
        return list;
    }

    public List<ProjectDTO> findAllWithImagesByCategoryUrl(final String categoryUrl) {
        List<ProjectDTO> list = new ArrayList<>();
        subgroupRepository.findAllByCategoryUrl(categoryUrl).stream()
                .flatMap(subgroup -> projectRepository.findAllBySubgroupTitle(subgroup.getTitle()).stream())
                .forEach(project -> populateImages(list, project));
        return list;
    }

    public List<ProjectDTO> findAllWithImagesBySubgroupUrl(final String subgroupUrl) {
        List<ProjectDTO> list = new ArrayList<>();
        projectRepository.findAllBySubgroupUrl(subgroupUrl)
                .forEach(project -> populateImages(list, project));
        return list;
    }

    private void populateImages(final List<ProjectDTO> list, final Project project) {
        final ProjectDTO projectDTO = projectMapper.map(project);
        if (projectDTO.isHasImages()) {
            final ImageDTO imageDTO = projectMapper.getAnyImage(project);
            imageDTO.setData(imageService.applyDataOnRead(imageDTO, projectDTO.getId()));
            projectDTO.setFirstImage(imageDTO);
            imageService.setProjectParents(projectDTO, project);
            list.add(projectDTO);
        }
    }

    public Page<ImageDTO> serveProjectToUser(final String projectUrl, final Optional<Integer> page) {
        final ProjectDTO projectDTO = findByUrl(projectUrl);
        final Project project = projectRepository.findByUrl(projectUrl);
        imageService.setProjectParents(projectDTO, project);
        final int currentPage = page.orElse(1);
        final PageRequest pageable = PageRequest.of(currentPage - 1, 1);
        return imageService.serveImagesOnReadPaginated(projectDTO, pageable);
    }

    public ProjectDTO viewProject(final String projectUrl) {
        final ProjectDTO projectDTO = findByUrl(projectUrl);
        projectDTO.setImages(imageService.serveImagesOnRead(projectDTO));
        final Project project = projectRepository.findByUrl(projectUrl);
        imageService.setProjectParents(projectDTO, project);
        return projectDTO;
    }



}
