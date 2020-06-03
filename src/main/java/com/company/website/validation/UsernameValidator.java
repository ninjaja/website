package com.company.website.validation;


import com.company.website.dto.UserDTO;
import com.company.website.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator for username uniqueness
 *
 * @author Dmitry Matrizaev
 * @since 07.05.2020
 */
@Service
@AllArgsConstructor
public class UsernameValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final UserDTO userDTO = (UserDTO) target;
        if (userService.existsByLogin(userDTO.getLogin())) {
            errors.reject("login.exists", "Login exists, please select another login");
        }
    }
}
