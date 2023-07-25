package in.boimama.readstories.dto;

public enum ResponseCode {
    SUCCESS(0000, "Success"),
    GENERIC_APPLICATION_ERROR(1001, "Generic Application Error"),
    GENERIC_CLIENT_VALIDATION_ERROR(1002, "Generic Client Validation Error"),
    GENERIC_SERVER_ERROR(1003, "Generic Server Error"),
    STORY_NOT_FOUND(1204, "Story Not Found");
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
