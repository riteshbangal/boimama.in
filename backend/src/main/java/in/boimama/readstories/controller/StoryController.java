package in.boimama.readstories.controller;

import in.boimama.readstories.service.StoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/story")
public class StoryController {

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

    @GetMapping(value = "/{id}/{title}")
    @ResponseBody
    public String getStory(@PathVariable(name = "id") String storyId, @PathVariable(name = "title") String storyTitle) {
        return storyService.getStory(storyId, storyTitle).getContent();
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