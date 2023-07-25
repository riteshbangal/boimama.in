package in.boimama.readstories.exception;

import in.boimama.readstories.dto.ErrorResponse;
import in.boimama.readstories.dto.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ErrorResponse(ResponseCode.GENERIC_SERVER_ERROR.getResponseCode(), exception.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Add more @ExceptionHandler methods for specific exception types as needed
}
