package in.boimama.readstories.controller;

import in.boimama.readstories.dto.Response;
import in.boimama.readstories.dto.StoryRequest;
import in.boimama.readstories.dto.StoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static in.boimama.readstories.dto.ResponseCode.GENERIC_APPLICATION_ERROR;
import static in.boimama.readstories.dto.ResponseCode.STORY_NOT_ADDED;
import static in.boimama.readstories.dto.ResponseCode.STORY_NOT_FOUND;

@RestController
@RequestMapping("/story")
@Validated // Enable validation for this controller
public class StoryController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(StoryController.class);

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

    @PostMapping(path = "/add", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE  }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Response> addStory(@Valid @ModelAttribute StoryRequest request) {
        final StoryResponse response = storyService.addStory(request);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(getErrorResponse(STORY_NOT_ADDED));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<Response> getStory(@UUID(message = "Story id must be a valid UUID") @PathVariable(name = "id") String storyId) {
        final StoryResponse response = storyService.getStory(storyId);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(STORY_NOT_FOUND));
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<Response> deleteStory(@UUID(message = "Story id must be a valid UUID") @PathVariable(name = "id") String storyId) {
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

    @GetMapping(path = "/{id}/image", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<?> getStoryCoverImage(@UUID(message = "Story id must be a valid UUID") @PathVariable(name = "id") String storyId) {
        final StoryResponse response = storyService.getStory(storyId);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(STORY_NOT_FOUND));
        }

        // Create HttpHeaders and set Content-Type, Content-Disposition headers
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Set the appropriate content type
        // headers.setContentDispositionFormData("attachment", storyId + ".jpg"); // Specify filename for download

        return new ResponseEntity<>(response.getImage(), headers, HttpStatus.OK);
    }
}