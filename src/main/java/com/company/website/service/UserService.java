package com.company.website.service;

import com.company.website.model.User;
import com.company.website.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Dmitry Matrizaev
 * @since 04.05.2020
 */
@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> findByLogin(String login) {
        return repository.findByLogin(login);
    }

    public User save(User user) {
        return repository.save(user);
    }

}
