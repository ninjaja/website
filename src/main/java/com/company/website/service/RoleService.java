package com.company.website.service;

import com.company.website.model.Role;
import com.company.website.repository.RoleRepository;
import org.springframework.stereotype.Service;

/**
 * @author Dmitry Matrizaev
 * @since 04.05.2020
 */
@Service
public class RoleService {

    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public Role findByName(String name) {
        return repository.findByName(name);
    }

}
