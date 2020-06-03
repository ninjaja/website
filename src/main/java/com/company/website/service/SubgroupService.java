package com.company.website.service;

import com.company.website.dto.CategoryDTO;
import com.company.website.dto.SubgroupDTO;
import com.company.website.model.Category;
import com.company.website.model.Subgroup;
import com.company.website.repository.CategoryRepository;
import com.company.website.repository.SubgroupRepository;
import com.company.website.service.mapping.CategoryMapper;
import com.company.website.service.mapping.SubgroupMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for subgroups
 *
 * @author Dmitry Matrizaev
 * @since 04.05.2020
 */
@Service
@Transactional
@AllArgsConstructor
public class SubgroupService {

    private final SubgroupRepository subgroupRepository;
    private final CategoryRepository categoryRepository;
    private final ProjectService projectService;
    private final CategoryMapper categoryMapper;
    private final SubgroupMapper subgroupMapper;

    public List<SubgroupDTO> findAllByCategoryUrl(final String categoryUrl) {
        return subgroupRepository.findAllByCategoryUrl(categoryUrl).stream()
                .map(subgroupMapper::map)
                .collect(Collectors.toList());
    }

    public void save(final SubgroupDTO subgroupDTO, final String categoryUrl) {
        final Subgroup subgroup = subgroupRepository.findById(subgroupDTO.getId()).orElseGet(Subgroup::new);
        subgroupMapper.copyFromDto(subgroupDTO, subgroup);
        subgroup.setCategory(categoryRepository.findByUrl(categoryUrl));
        subgroupRepository.save(subgroup);
    }

    public void deleteByUrl(final String url) {
        subgroupRepository.removeByUrl(url);
    }

    public SubgroupDTO findByUrl(final String url) {
        return subgroupMapper.map(subgroupRepository.findByUrl(url));
    }

    public SubgroupDTO findById(final Integer id) {
        return subgroupMapper.map(subgroupRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    public void copyDTOValuesOnEditError(final SubgroupDTO subgroupDTO) {
        final SubgroupDTO oldSubgroupDTO = findById(subgroupDTO.getId());
        subgroupDTO.setTitle(oldSubgroupDTO.getTitle());
        subgroupDTO.setUrl(oldSubgroupDTO.getUrl());
        subgroupDTO.setDescription(oldSubgroupDTO.getDescription());
    }

    public SubgroupDTO viewSubgroup(final String subgroupUrl) {
        final SubgroupDTO subgroupDTO = findByUrl(subgroupUrl);
        final Subgroup subgroup = subgroupRepository.findByUrl(subgroupUrl);
        subgroupDTO.setCategory(categoryMapper.map(subgroup.getCategory()));
        subgroupDTO.setProjects(projectService.findAllBySubgroupUrl(subgroupUrl));
        return subgroupDTO;
    }

    public SubgroupDTO viewSubgroupUserPages(final String subgroupUrl) {
        final SubgroupDTO subgroupDTO = findByUrl(subgroupUrl);
        final Subgroup subgroup = subgroupRepository.findByUrl(subgroupUrl);
        final Category category = subgroup.getCategory();
        final CategoryDTO categoryDTO = categoryMapper.map(category);
        final List<SubgroupDTO> subgroups = findAllByCategoryUrl(categoryDTO.getUrl());
        categoryDTO.setSubgroups(subgroups);
        subgroupDTO.setCategory(categoryDTO);
        subgroupDTO.setProjects(projectService.findAllWithImagesBySubgroupUrl(subgroupUrl));
        return subgroupDTO;
    }

}
