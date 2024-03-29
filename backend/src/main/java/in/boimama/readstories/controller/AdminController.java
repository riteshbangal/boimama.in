package in.boimama.readstories.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static in.boimama.readstories.dto.ResponseCode.GENERIC_APPLICATION_ERROR;

@RestController
@RequestMapping("/admin")
@Validated // Enable validation for this controller
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping(value = "/health")
    public String healthCheck() {
        return "Healthy";
    }

    @DeleteMapping(value = "/delete-all-authors")
    @ResponseBody
    public ResponseEntity<?> deleteAllAuthors() {
        // TODO: Fix this. Caused by: com.datastax.oss.driver.api.core.servererrors.WriteTimeoutException:
        //  Cassandra timeout during SIMPLE write query at consistency LOCAL_QUORUM (2 replica were required but only 0 acknowledged the write)
        if (authorService.deleteAllAuthors()) {
            return ResponseEntity.ok().body("Deleted all authors!");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getErrorResponse(GENERIC_APPLICATION_ERROR));
    }

    @DeleteMapping(value = "/delete-all-stories")
    @ResponseBody
    public ResponseEntity<?> deleteAllStories() {
        // TODO: Fix this. Caused by: com.datastax.oss.driver.api.core.servererrors.WriteTimeoutException:
        //  Cassandra timeout during SIMPLE write query at consistency LOCAL_QUORUM (2 replica were required but only 0 acknowledged the write)
        if (storyService.deleteAllStories()) {
            return ResponseEntity.ok().body("Deleted all stories!");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getErrorResponse(GENERIC_APPLICATION_ERROR));
    }
}