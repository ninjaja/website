package com.company.website.controller.user;

import com.company.website.dto.UserDTO;
import com.company.website.service.user.UserService;
import com.company.website.validation.UsernameValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

import static com.company.website.controller.constants.ControllerConstants.REDIRECT_TO_MAIN;
import static com.company.website.controller.constants.ControllerConstants.REGISTRATION;
import static com.company.website.controller.constants.ControllerConstants.USER_DTO;

/**
 * New user registration controller. Initial admin should be created as a regular user and his role should be changed
 * from DB manually
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
@Controller
@AllArgsConstructor
public class RegistrationController {

    private final UserService userService;
    private final UsernameValidator usernameValidator;

    @GetMapping("/registration")
    public String registration(final UserDTO userDTO, final Model model) {
        model.addAttribute(USER_DTO, userDTO);
        return REGISTRATION;
    }

    @PostMapping("/registration")
    public String addUser(@Valid final UserDTO userDTO, final BindingResult result, final Model model) {
        model.addAttribute(USER_DTO, userDTO);
        usernameValidator.validate(userDTO, result);
        if (result.hasErrors()) {
            return REGISTRATION;
        }
        userService.save(userDTO);
        return REDIRECT_TO_MAIN;
    }

}
