package com.company.website.validation.title;

import com.company.website.dto.SubgroupDTO;
import com.company.website.repository.SubgroupRepository;

/**
 * @author Dmitry Matrizaev
 * @since 08.05.2020
 */
public class SubgroupTitleValidator extends CustomTitleValidator<SubgroupDTO, SubgroupRepository> {

   public SubgroupTitleValidator(SubgroupRepository repository) {
      super(repository);
   }

}
