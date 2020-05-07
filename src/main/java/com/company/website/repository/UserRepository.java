package com.company.website.repository;

import com.company.website.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByLogin(String login);

    boolean existsByLogin(String login);
}
