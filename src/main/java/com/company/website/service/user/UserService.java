package com.company.website.service.user;

import com.company.website.dto.RoleDTO;
import com.company.website.dto.UserDTO;
import com.company.website.model.User;
import com.company.website.repository.UserRepository;
import com.company.website.service.mapping.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByLogin(String login) {
        return repository.findByLogin(login);
    }

    public User save(UserDTO userDTO, RoleDTO roleDTO) {
        userDTO.setRoles(Collections.singleton(roleDTO));
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return repository.save(mapper.map(userDTO));
    }

    public boolean existsByLogin(String login) {
        return repository.existsByLogin(login);
    }

}
