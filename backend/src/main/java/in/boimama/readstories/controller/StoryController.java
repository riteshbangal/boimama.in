package in.boimama.readstories.controller;

import in.boimama.readstories.dto.Response;
import in.boimama.readstories.dto.StoryRequest;
import in.boimama.readstories.dto.StoryResponse;
import in.boimama.readstories.exception.ApplicationException;
import in.boimama.readstories.service.StoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

import static in.boimama.readstories.dto.ResponseCode.GENERIC_APPLICATION_ERROR;
import static in.boimama.readstories.dto.ResponseCode.STORY_NOT_ADDED;
import static in.boimama.readstories.dto.ResponseCode.STORY_NOT_FOUND;

@RestController
@RequestMapping("/story")
public class StoryController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(StoryController.class);

    @Autowired
    StoryService storyService;

    @Operation(description = "Health check API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Healthy"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Unhealthy")
    })
    @GetMapping(value = "/health")
    public String healthCheck() {
        return "Healthy";
    }

    // TODO: Temporary. Delete me!
    @RequestMapping("/addt")
    @ResponseBody
    public String addStoryTemp() {
        try {
            storyService.addStory();
        } catch (ApplicationException e) {
            return "Added story failed";
        }
        return "Added story successfully";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Response> addStory(@RequestBody StoryRequest request) {
        final StoryResponse response = new StoryResponse();
        response.setTitle(request.getTitle());
        response.setDescription(request.getDescription());
        response.setContent(request.getContent());
        response.setCategory(request.getCategory());


        //final StoryResponse response = storyService.addStory(request);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(getErrorResponse(STORY_NOT_ADDED));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<Response> getStory(@PathVariable(name = "id") String storyId) {
        final StoryResponse response = storyService.getStory(storyId);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(STORY_NOT_FOUND));
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<Response> deleteStory(@PathVariable(name = "id") String storyId) {
        /**
         * Check if the story is present against the id,
         * And if presents, then do delete operation. Else, return 404.
         */
        final StoryResponse storyResponse = storyService.getStory(storyId);
        if (storyResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(STORY_NOT_FOUND));
        }

        if (storyService.deleteStory(storyId)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(storyResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getErrorResponse(GENERIC_APPLICATION_ERROR));
    }




    @GetMapping(
            value = "/story/image",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    @ResponseBody
    public byte[] getStoryImage(@RequestParam(name = "id") String storyId, @RequestParam(name = "title") String storyTitle) {
        return storyService.getStory(storyId, storyTitle).getImage();
    }
}