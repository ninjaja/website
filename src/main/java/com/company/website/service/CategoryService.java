package com.company.website.service;

import com.company.website.dto.CategoryDTO;
import com.company.website.model.Category;
import com.company.website.repository.CategoryRepository;
import com.company.website.service.mapping.CategoryMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Dmitry Matrizaev
 * @since 04.05.2020
 */
@Service
@Transactional
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public Iterable<CategoryDTO> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    public void save(CategoryDTO categoryDTO) {
        Category category = repository.findById(categoryDTO.getId()).orElseGet(Category::new);
        category = mapper.copyFromDto(categoryDTO, category);
        repository.save(category);
    }

    public void deleteByTitle(String title) {
        repository.removeByTitle(title);
    }

    public CategoryDTO findByUrl(String url) {
        return mapper.map(repository.findByUrl(url));
    }

    public Optional<CategoryDTO> findById(Integer id) {
        return Optional.of(mapper.map(repository.findById(id).orElseThrow(EntityNotFoundException::new)));
    }

}
