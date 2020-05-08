package com.company.website.dto;

import com.company.website.validation.title.UniqueTitle;
import com.company.website.validation.url.UniqueURL;
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
@UniqueTitle
@UniqueURL
public class SubgroupDTO implements EntityDTO {

    private int id;

    @NotNull
    @Size(min = 2, max = 50)
    private String title;

    @NotNull
    @Size(min = 2, max = 50)
    private String url;

    @Size(max = 1000)
    private String description;

    private CategoryDTO category;

    private Set<ProjectDTO> projects;

}
