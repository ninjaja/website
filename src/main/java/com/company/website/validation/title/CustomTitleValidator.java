package com.company.website.validation.title;

import com.company.website.dto.EntityDTO;
import com.company.website.model.CustomEntity;
import com.company.website.repository.CustomRepository;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Dmitry Matrizaev
 * @since 08.05.2020
 */
@AllArgsConstructor
public class CustomTitleValidator<T extends EntityDTO, U extends CustomRepository>
        implements ConstraintValidator<UniqueTitle, T> {

    private final U repository;

    @Override
    public void initialize(UniqueTitle constraint) {
    }

    @Override
    public boolean isValid(T t, ConstraintValidatorContext context) {
        final boolean existsByTitle = repository.existsByTitle(t.getTitle());
        if (existsByTitle) {
            final CustomEntity entityFromDatabase = repository.findByTitle(t.getTitle());
            final int idFromDatabase = entityFromDatabase.getId();
            final int idFromDto = t.getId();
            return idFromDto == idFromDatabase;
        }
        return true;
    }

}
