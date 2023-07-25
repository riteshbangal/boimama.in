package in.boimama.readstories.dto;

import java.time.LocalDateTime;

public class ErrorResponse implements Response {
    private int errorCode;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(int errorCode, String message) {
        this(errorCode, message, LocalDateTime.now());
    }

    public ErrorResponse(int errorCode, String message, LocalDateTime timestamp) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
