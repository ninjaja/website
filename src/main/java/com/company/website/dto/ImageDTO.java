package com.company.website.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
@Getter
@Setter
@NoArgsConstructor
public class ImageDTO {

    private int id;

    @NotNull
    @Size(min = 2, max = 30)
    private String title;

    private String data;

    private ProjectDTO project;

}
