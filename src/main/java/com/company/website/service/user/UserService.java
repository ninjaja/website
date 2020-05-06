package com.company.website.service.user;

import com.company.website.dto.UserDTO;
import com.company.website.model.User;
import com.company.website.repository.UserRepository;
import com.company.website.service.mapping.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Dmitry Matrizaev
 * @since 04.05.2020
 */
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public Optional<User> findByLogin(String login) {
        return repository.findByLogin(login);
    }

    public User save(UserDTO user) {
        return repository.save(mapper.map(user));
    }

}
