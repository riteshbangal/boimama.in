package in.boimama.readstories.exception;

import in.boimama.readstories.dto.ErrorResponse;
import in.boimama.readstories.dto.ResponseCode;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ErrorResponse(ResponseCode.GENERIC_SERVER_ERROR.getResponseCode(),
                exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleException(ApplicationException exception) {
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ErrorResponse(ResponseCode.GENERIC_SERVER_ERROR.getResponseCode(),
                exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException exception) {
        logger.error(exception.getMessage(), exception);
        final Map<String, String> errorMessages = new HashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> errorMessages.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(new ErrorResponse(ResponseCode.GENERIC_CLIENT_VALIDATION_ERROR.getResponseCode(),
                errorMessages.values().toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleValidationException(ConstraintViolationException exception) {
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ErrorResponse(ResponseCode.GENERIC_CLIENT_VALIDATION_ERROR.getResponseCode(),
                exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
