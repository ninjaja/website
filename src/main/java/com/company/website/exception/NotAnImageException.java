package com.company.website.exception;

/**
 * @author Dmitry Matrizaev
 * @since 27.04.2020
 */
public class NotAnImageException extends RuntimeException {

    public NotAnImageException(String message) {
        super(message);
    }

    public NotAnImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAnImageException() {}

}
