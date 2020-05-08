package com.company.website.validation.url;

import com.company.website.dto.CategoryDTO;
import com.company.website.repository.CategoryRepository;

/**
 * @author Dmitry Matrizaev
 * @since 08.05.2020
 */
public class CategoryURLValidator extends CustomURLValidator<CategoryDTO, CategoryRepository> {

    public CategoryURLValidator(CategoryRepository repository) {
        super(repository);
    }

}
