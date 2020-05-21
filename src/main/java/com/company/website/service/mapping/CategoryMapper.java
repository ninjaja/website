package com.company.website.service.mapping;

import com.company.website.dto.CategoryDTO;
import com.company.website.model.Category;
import org.springframework.stereotype.Component;

/**
 * Mapper for Category entities and DTO
 *
 * @author Dmitry Matrizaev
 * @since 05.05.2020
 */
@Component
public class CategoryMapper {

    public Category map(final CategoryDTO dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setTitle(dto.getTitle());
        category.setUrl(dto.getUrl());
        category.setDescription(dto.getDescription());
        return category;
    }

    public CategoryDTO map(final Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setTitle(category.getTitle());
        dto.setUrl(category.getUrl());
        dto.setDescription(category.getDescription());
        return dto;
    }

    public void copyFromDto(final CategoryDTO dto, final Category category) {
        category.setTitle(dto.getTitle());
        category.setUrl(dto.getUrl());
        category.setDescription(dto.getDescription());
    }

}
