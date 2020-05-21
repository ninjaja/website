package com.company.website.validation.title;

import com.company.website.dto.CategoryDTO;
import com.company.website.repository.CategoryRepository;

/**
 * Validator for category titles uniqueness
 *
 * @author Dmitry Matrizaev
 * @since 08.05.2020
 */
public class CategoryTitleValidator extends CustomTitleValidator<CategoryDTO, CategoryRepository> {

   public CategoryTitleValidator(CategoryRepository repository) {
      super(repository);
   }
}
