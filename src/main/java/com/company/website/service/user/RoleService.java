package com.company.website.service.user;

import com.company.website.model.Role;
import com.company.website.repository.RoleRepository;
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

    public Role findByName(String name) {
        return repository.findByName(name);
    }

}
