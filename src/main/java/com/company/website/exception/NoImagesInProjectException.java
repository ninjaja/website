package com.company.website.exception;

/**
 * @author Dmitry Matrizaev
 * @since 27.04.2020
 */
public class NoImagesInProjectException extends RuntimeException {

    public NoImagesInProjectException(String message) {
        super(message);
    }

    public NoImagesInProjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoImagesInProjectException() {}

}
