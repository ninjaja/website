package com.company.website.service.user;

import com.company.website.dto.RoleDTO;
import com.company.website.repository.RoleRepository;
import com.company.website.service.mapping.RoleMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Dmitry Matrizaev
 * @since 04.05.2020
 */
@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository repository;
    private final RoleMapper mapper;

    public RoleDTO findByName(String name) {
        return mapper.map(repository.findByName(name));
    }

}
