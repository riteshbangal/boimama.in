package in.boimama.readstories.controller;

import in.boimama.readstories.dto.ErrorResponse;
import in.boimama.readstories.dto.ResponseCode;
import in.boimama.readstories.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractController {

    @Autowired
    StoryService storyService;

    protected ErrorResponse getErrorResponse(ResponseCode responseCode) {
        return new ErrorResponse(responseCode.getResponseCode(), responseCode.getMessage());
    }
}
