package com.company.website.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * DTO for Role entities
 *
 * @author Dmitry Matrizaev
 * @since 21.04.2020
 */
@Getter
@Setter
@NoArgsConstructor
public class RoleDTO {

    @NotNull
    @Size(min = 2, max = 10)
    private String name;

}
