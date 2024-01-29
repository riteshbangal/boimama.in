package in.boimama.readstories.controller;

import in.boimama.readstories.dto.ContactUsRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static in.boimama.readstories.utils.ApplicationConstants.FAILURE_MESSAGE;
import static in.boimama.readstories.utils.ApplicationUtils.isEmpty;

@RestController
//@CrossOrigin(origins = "http://127.0.0.1:5500") // TODO: Temporary Change
@RequestMapping("/user")
@Validated // Enable validation for this controller
public class UserController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

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

    @PostMapping(path = "/contact", consumes = { MediaType.APPLICATION_JSON_VALUE  }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> contactUser(@Valid @RequestBody ContactUsRequest pContactUsRequest) {

        final String response = userService.contactUser(pContactUsRequest);
        final String jsonResponse = "{\"response\": \""
                + (isEmpty(response) ? FAILURE_MESSAGE : response) + "\"}"; // Create a JSON response

        if (isEmpty(response)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
        }
        return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);
    }
}