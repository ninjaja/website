package com.company.website.service.mapping;

import com.company.website.dto.SubgroupDTO;
import com.company.website.model.Subgroup;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Dmitry Matrizaev
 * @since 05.05.2020
 */
@Component
@AllArgsConstructor
public class SubgroupMapper {

    public Subgroup map(SubgroupDTO dto) {
        Subgroup subgroup = new Subgroup();
        subgroup.setId(dto.getId());
        subgroup.setTitle(dto.getTitle());
        subgroup.setUrl(dto.getUrl());
        subgroup.setDescription(dto.getDescription());
        return subgroup;
    }

    public SubgroupDTO map(Subgroup subgroup) {
        SubgroupDTO dto = new SubgroupDTO();
        dto.setId(subgroup.getId());
        dto.setTitle(subgroup.getTitle());
        dto.setUrl(subgroup.getUrl());
        dto.setDescription(subgroup.getDescription());
        return dto;
    }

    public Subgroup copyFromDto(final SubgroupDTO dto, final Subgroup subgroup) {
        subgroup.setTitle(dto.getTitle());
        subgroup.setUrl(dto.getUrl());
        subgroup.setDescription(dto.getDescription());
        return subgroup;
    }

}
