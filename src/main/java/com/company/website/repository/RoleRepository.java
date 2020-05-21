package com.company.website.repository;

import com.company.website.model.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for Role entities
 *
 * @author Dmitry Matrizaev
 * @since 21.04.2020
 */
public interface RoleRepository extends CrudRepository<Role, Integer> {

    Role findByName(String name);
}
