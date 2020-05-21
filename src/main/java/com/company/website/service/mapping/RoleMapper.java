package com.company.website.service.mapping;

import com.company.website.dto.RoleDTO;
import com.company.website.model.Role;
import org.springframework.stereotype.Component;

/**
 * Mapper for Role entities and DTO
 *
 * @author Dmitry Matrizaev
 * @since 05.05.2020
 */
@Component
public class RoleMapper {

    public Role map(RoleDTO dto) {
        Role role = new Role();
        role.setName(dto.getName());
        return role;
    }

    public RoleDTO map(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setName(role.getName());
        return dto;
    }

}
