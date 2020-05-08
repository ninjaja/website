package com.company.website.validation.url;

import com.company.website.dto.SubgroupDTO;
import com.company.website.repository.SubgroupRepository;

/**
 * @author Dmitry Matrizaev
 * @since 08.05.2020
 */
public class SubgroupURLValidator extends CustomURLValidator<SubgroupDTO, SubgroupRepository> {

    public SubgroupURLValidator(SubgroupRepository repository) {
        super(repository);
    }

}
