package com.company.website.dto;

import com.company.website.validation.title.UniqueTitle;
import com.company.website.validation.url.UniqueURL;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * DTO for Project entities
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
@Getter
@Setter
@NoArgsConstructor
@UniqueTitle
@UniqueURL
public class ProjectDTO implements CustomDTO {

    private int id;

    @NotNull
    @Size(min = 2, max = 50)
    private String title;

    @NotNull
    @Size(min = 2, max = 50)
    private String url;

    @Size(max = 1000)
    private String description;

    private String dateCreated;

    private SubgroupDTO subgroup;

    private List<ImageDTO> images;

    private ImageDTO firstImage;

    private boolean hasImages;

}
