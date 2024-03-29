package in.boimama.readstories.dto;

public enum ResponseCode {
    SUCCESS(0000, "Success"),
    GENERIC_APPLICATION_ERROR(1001, "Generic Application Error"),
    GENERIC_CLIENT_VALIDATION_ERROR(1002, "Generic Client Validation Error"),
    GENERIC_SERVER_ERROR(1003, "Generic Server Error"),

    AUTHOR_NOT_FOUND(1204, "Author Not Found"),
    AUTHOR_NOT_ADDED(1205, "Author Not added"),
    AUTHOR_NOT_UPDATED(1206, "Author Not updated"),
    // Add more response codes as needed.

    STORY_NOT_FOUND(1204, "Story Not Found"),
    STORY_NOT_ADDED(1205, "Story Not added"),
    STORY_NOT_UPDATED(1206, "Story Not updated");
    // Add more response codes as needed.

    private final int responseCode;
    private final String message;

    ResponseCode(int responseCode, String message) {
        this.responseCode = responseCode;
        this.message = message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getMessage() {
        return message;
    }
}
