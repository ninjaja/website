package com.company.website.dto;

/**
 * Interface for DTOs requiring @UniqueTitle and @UniqueURL validations
 *
 * @author Dmitry Matrizaev
 * @since 08.05.2020
 */
public interface CustomDTO {

    int getId();

    String getTitle();

    String getUrl();

}
