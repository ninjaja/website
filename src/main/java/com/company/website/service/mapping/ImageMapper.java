package com.company.website.service.mapping;

import com.company.website.dto.ImageDTO;
import com.company.website.model.Image;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Dmitry Matrizaev
 * @since 05.05.2020
 */
@Component
@AllArgsConstructor
public class ImageMapper {

    public Image map(ImageDTO dto) {
        Image image = new Image();
        image.setId(dto.getId());
        image.setTitle(dto.getTitle());
        return image;
    }

    public ImageDTO map(Image image) {
        ImageDTO dto = new ImageDTO();
        dto.setId(image.getId());
        dto.setTitle(image.getTitle());
        return dto;
    }

    public Image copyFromDTO(ImageDTO dto, Image image) {
        image.setTitle(dto.getTitle());
        return image;
    }

}
