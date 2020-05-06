package com.company.website.controller.user;

import com.company.website.dto.RoleDTO;
import com.company.website.dto.UserDTO;
import com.company.website.model.User;
import com.company.website.service.user.RoleService;
import com.company.website.service.user.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/registration")
    public String registration() {
        return "common/registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid UserDTO user, Model model) {
        User userFromDb = userService.findByLogin(user.getLogin()).orElseThrow(() ->
                new UsernameNotFoundException("Login " + user.getLogin() + " not found"));
        if (Objects.nonNull(userFromDb)) {
            model.addAttribute("message", "Login exists, please select another login");
            return "common/registration";
        }
        RoleDTO role = roleService.findByName("USER");
        user.setRoles(Collections.singleton(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/";
    }

}
