package com.company.website.validation.url;

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
public class CustomURLValidator<T extends EntityDTO, U extends CustomRepository>
        implements ConstraintValidator<UniqueURL, T> {

    private final U repository;

    @Override
    public void initialize(UniqueURL constraint) {
    }

    @Override
    public boolean isValid(T t, ConstraintValidatorContext context) {
        final boolean existsByUrl = repository.existsByUrl(t.getUrl());
        if (existsByUrl) {
            final CustomEntity entityFromDatabase = repository.findByUrl(t.getUrl());
            final int idFromDatabase = entityFromDatabase.getId();
            final int idFromDto = t.getId();
            return idFromDto == idFromDatabase;
        }
        return true;
    }

}
