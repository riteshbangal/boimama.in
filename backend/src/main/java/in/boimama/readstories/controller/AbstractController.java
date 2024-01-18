package in.boimama.readstories.controller;

import in.boimama.readstories.dto.ErrorResponse;
import in.boimama.readstories.dto.ResponseCode;
import in.boimama.readstories.service.AuthorService;
import in.boimama.readstories.service.StoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractController {

    @Autowired
    AuthorService authorService;

    @Autowired
    StoryService storyService;

    protected ErrorResponse getErrorResponse(ResponseCode responseCode) {
        return new ErrorResponse(responseCode.getResponseCode(), responseCode.getMessage());
    }

    protected String getServerPath(HttpServletRequest pHttpRequest) {
        return pHttpRequest.getScheme() + "://" + pHttpRequest.getServerName() + ":" + pHttpRequest.getServerPort();
    }

    protected String getContextPath(HttpServletRequest pHttpRequest) {
        return pHttpRequest.getContextPath();
    }
}
