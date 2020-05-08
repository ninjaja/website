package com.company.website.validation.title;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Dmitry Matrizaev
 * @since 08.05.2020
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CategoryTitleValidator.class, SubgroupTitleValidator.class, ProjectTitleValidator.class})
public @interface UniqueTitle {

    String message() default "Title already exists, please select another one";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
