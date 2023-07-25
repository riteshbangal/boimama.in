package in.boimama.readstories.controller;

import in.boimama.readstories.dto.Response;
import in.boimama.readstories.dto.StoryResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

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

    @RequestMapping("/add")
    @ResponseBody
    public String addStory() {
        try {
            storyService.addStory();
        } catch (IOException e) {
            return "Added story failed";
        }
        return "Added story successfully";
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

    @GetMapping(value = "/{id}/{name}")
    @ResponseBody
    public String getStory(@PathVariable(name = "id") String storyId, @PathVariable(name = "name") String storyName) {
        return storyService.getStory(storyId, storyName).getContent();
    }

    @GetMapping(
            value = "/story/image",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    @ResponseBody
    public byte[] getStoryImage(@RequestParam(name = "id") String storyId, @RequestParam(name = "title") String storyTitle) {
        return storyService.getStory(storyId, storyTitle).getImage();
    }


    @RequestMapping("/count")
    @ResponseBody
    public String getCount() {
        return storyService.getCount();
    }

    @GetMapping(value = "/add/user/{uname}")
    @ResponseBody
    public String addUser(@PathVariable(value = "uname") String uname) {
        storyService.addUser(uname);
        return "Success";
    }
}