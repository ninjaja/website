package com.company.website.repository;

import com.company.website.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByLogin(String login);
}
