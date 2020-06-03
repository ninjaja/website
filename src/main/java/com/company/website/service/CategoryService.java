package com.company.website.service;

import com.company.website.dto.CategoryDTO;
import com.company.website.dto.SubgroupDTO;
import com.company.website.model.Category;
import com.company.website.repository.CategoryRepository;
import com.company.website.service.mapping.CategoryMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service layer for categories
 *
 * @author Dmitry Matrizaev
 * @since 04.05.2020
 */
@Service
@Transactional
@AllArgsConstructor
public class CategoryService {

    private final SubgroupService subgroupService;
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public List<CategoryDTO> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    public void save(final CategoryDTO categoryDTO) {
        final Category category = repository.findById(categoryDTO.getId()).orElseGet(Category::new);
        mapper.copyFromDto(categoryDTO, category);
        repository.save(category);
    }

    public void deleteByTitle(final String title) {
        repository.removeByTitle(title);
    }

    public CategoryDTO findByUrl(final String url) {
        return mapper.map(repository.findByUrl(url));
    }

    public CategoryDTO findById(final Integer id) {
        return mapper.map(repository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    public void copyDTOValuesOnEditError(final CategoryDTO categoryDTO) {
        final CategoryDTO oldCategoryDTO = findById(categoryDTO.getId());
        categoryDTO.setTitle(oldCategoryDTO.getTitle());
        categoryDTO.setUrl(oldCategoryDTO.getUrl());
        categoryDTO.setDescription(oldCategoryDTO.getDescription());
    }

    public CategoryDTO viewCategory(final String categoryUrl) {
        final CategoryDTO categoryDTO = findByUrl(categoryUrl);
        final List<SubgroupDTO> subgroups = subgroupService.findAllByCategoryUrl(categoryUrl);
        categoryDTO.setSubgroups(subgroups);
        return categoryDTO;
    }

}
