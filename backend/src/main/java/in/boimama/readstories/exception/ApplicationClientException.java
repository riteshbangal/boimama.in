/*
 * Copyright (c) 2023, Ritesh. All rights reserved.
 */
package in.boimama.readstories.exception;


/**
 * This exception class is used to handle some client validation errors or bad request related error.
 * This class is wrapper over Runtime Exception.
 * Packer will throw a runtime exception, if incorrect parameters are being passed.
 *
 * @author - Ritesh
 * @version 1.0
 * @since <24-July-2023>
 */
public class ApplicationClientException extends RuntimeException {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Generic exception.
     *
     * @param errorMessage the error message
     */
    public ApplicationClientException(final String errorMessage) {
        super(errorMessage);
    }

    /**
     * Instantiates a new Generic exception.
     *
     * @param errorMessage the error message
     * @param cause        the cause
     */
    public ApplicationClientException(final String errorMessage, final Throwable cause) {
        super(errorMessage, cause);
    }

    /**
     * Constructs a new exception with the specified cause
     *
     * @param cause the cause
     */

    public ApplicationClientException(Throwable cause) {
        super(cause);
    }
}