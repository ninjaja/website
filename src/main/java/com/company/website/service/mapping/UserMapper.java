package com.company.website.service.mapping;

import com.company.website.dto.UserDTO;
import com.company.website.model.User;
import org.springframework.stereotype.Component;

/**
 * Mapper for User entities and DTO
 *
 * @author Dmitry Matrizaev
 * @since 05.05.2020
 */
@Component
public class UserMapper {

    public User map(UserDTO dto) {
        User user = new User();
        user.setLogin(dto.getLogin());
        user.setPassword(dto.getPassword());
        return user;
    }

    public UserDTO map(User user) {
        UserDTO dto = new UserDTO();
        dto.setLogin(user.getLogin());
        dto.setPassword(user.getPassword());
        return dto;
    }

}
