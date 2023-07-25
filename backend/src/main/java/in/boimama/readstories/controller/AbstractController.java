package in.boimama.readstories.controller;

import in.boimama.readstories.dto.ErrorResponse;
import in.boimama.readstories.dto.ResponseCode;

public abstract class AbstractController {
    protected ErrorResponse getErrorResponse(ResponseCode responseCode) {
        return new ErrorResponse(responseCode.getResponseCode(), responseCode.getMessage());
    }
}
