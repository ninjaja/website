package com.company.website.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    @NotNull
    @Size(min = 4, max = 30)
    private String login;

    @NotNull
    @Size(min = 4, max = 255)
    private String password;

    private Set<RoleDTO> roles;

}
