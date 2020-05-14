package com.company.website.service;

import com.company.website.dto.CategoryDTO;
import com.company.website.dto.SubgroupDTO;
import com.company.website.model.Subgroup;
import com.company.website.repository.CategoryRepository;
import com.company.website.repository.SubgroupRepository;
import com.company.website.service.mapping.SubgroupMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dmitry Matrizaev
 * @since 04.05.2020
 */
@Service
@Transactional
@AllArgsConstructor
public class SubgroupService {

    private final SubgroupRepository subgroupRepository;
    private final CategoryRepository categoryRepository;
    private final SubgroupMapper subgroupMapper;

    public List<SubgroupDTO> findAllByCategory(CategoryDTO category) {
        return subgroupRepository.findAllByCategoryTitle(category.getTitle()).stream()
                .map(subgroupMapper::map)
                .collect(Collectors.toList());
    }

    public void save(SubgroupDTO subgroupDTO, CategoryDTO categoryDTO) {
        Subgroup subgroup = subgroupRepository.findById(subgroupDTO.getId()).orElseGet(Subgroup::new);
        subgroup = subgroupMapper.copyFromDto(subgroupDTO, subgroup);
        subgroup.setCategory(categoryRepository.findByTitle(categoryDTO.getTitle()));
        subgroupRepository.save(subgroup);
    }

    public void deleteByUrl(String url) {
        subgroupRepository.removeByUrl(url);
    }

    public SubgroupDTO findByUrl(String url) {
        return subgroupMapper.map(subgroupRepository.findByUrl(url));
    }

    public SubgroupDTO findById(Integer id) {
        return subgroupMapper.map(subgroupRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    public void copyDTOValuesOnEditError(final SubgroupDTO subgroupDTO) {
        final SubgroupDTO oldSubgroupDTO = findById(subgroupDTO.getId());
        subgroupDTO.setTitle(oldSubgroupDTO.getTitle());
        subgroupDTO.setUrl(oldSubgroupDTO.getUrl());
        subgroupDTO.setDescription(oldSubgroupDTO.getDescription());
    }

}
