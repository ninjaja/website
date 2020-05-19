package com.company.website.service.user;

import com.company.website.dto.UserDTO;
import com.company.website.model.Role;
import com.company.website.model.User;
import com.company.website.repository.UserRepository;
import com.company.website.service.mapping.RoleMapper;
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
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByLogin(String login) {
        return repository.findByLogin(login);
    }

    public User save(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        final User user = userMapper.map(userDTO);
        Role role = roleService.findByName("USER");
        user.setRoles(Collections.singleton(role));
        return repository.save(user);
    }

    public boolean existsByLogin(String login) {
        return repository.existsByLogin(login);
    }

}
