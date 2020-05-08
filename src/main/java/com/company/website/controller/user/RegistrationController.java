package com.company.website.controller.user;

import com.company.website.dto.RoleDTO;
import com.company.website.dto.UserDTO;
import com.company.website.service.user.RoleService;
import com.company.website.service.user.UserService;
import com.company.website.validation.UsernameValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
@Controller
@AllArgsConstructor
public class RegistrationController {

    private final UserService userService;
    private final RoleService roleService;
    private final UsernameValidator validator;

    @GetMapping("/registration")
    public String registration(final UserDTO userDTO, final Model model) {
        model.addAttribute("userDTO", userDTO);
        return "common/registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid final UserDTO userDTO, final BindingResult result, final Model model) {
        model.addAttribute("userDTO", userDTO);
        validator.validate(userDTO, result);
        if (result.hasErrors()) {
            return "common/registration";
        }
        RoleDTO roleDTO = roleService.findByName("USER");
        userService.save(userDTO, roleDTO);
        return "redirect:/";
    }

}
