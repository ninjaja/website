package com.company.website.controller.user;

import com.company.website.model.Role;
import com.company.website.model.User;
import com.company.website.repository.RoleRepository;
import com.company.website.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Objects;

/**
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String registration() {
        return "common/registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid User user, Model model) {
        User userFromDb = userRepository.findByLogin(user.getLogin());
        if (Objects.nonNull(userFromDb)) {
            model.addAttribute("message", "Login exists, please select another login");
            return "common/registration";
        }
        Role role = roleRepository.findByName("USER");
        user.setRoles(Collections.singleton(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/";
    }
}
