package in.boimama.readstories.controller;

import in.boimama.readstories.dto.AuthorRequest;
import in.boimama.readstories.dto.AuthorResponse;
import in.boimama.readstories.dto.Response;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static in.boimama.readstories.dto.ResponseCode.AUTHOR_NOT_ADDED;
import static in.boimama.readstories.dto.ResponseCode.AUTHOR_NOT_FOUND;
import static in.boimama.readstories.dto.ResponseCode.AUTHOR_NOT_UPDATED;
import static in.boimama.readstories.dto.ResponseCode.GENERIC_APPLICATION_ERROR;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500") // TODO: Temporary Change
@RequestMapping("/author")
@Validated // Enable validation for this controller
public class AuthorController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);

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
    public ResponseEntity<Response> addAuthor(@Valid @ModelAttribute("authorRequest") AuthorRequest pAuthorRequest) {
        final AuthorResponse response = authorService.addAuthor(pAuthorRequest);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(getErrorResponse(AUTHOR_NOT_ADDED));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<Response> getAuthorDetails(@UUID(message = "Author id must be a valid UUID") @PathVariable(name = "id") String authorId) {
        final AuthorResponse response = authorService.getAuthor(authorId);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(AUTHOR_NOT_FOUND));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/all")
    @ResponseBody
    public ResponseEntity<?> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<Response> deleteAuthor(@UUID(message = "Author id must be a valid UUID") @PathVariable(name = "id") String authorId) {
        /**
         * Check if the author is present against the id,
         * And if presents, then do delete operation. Else, return 404.
         */
        final AuthorResponse authorResponse = authorService.getAuthor(authorId);
        if (authorResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(AUTHOR_NOT_FOUND));
        }

        if (authorService.deleteAuthor(authorId)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(authorResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getErrorResponse(GENERIC_APPLICATION_ERROR));
    }

    @GetMapping(path = "/{id}/image", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<?> getAuthorCoverImage(@UUID(message = "Author id must be a valid UUID") @PathVariable(name = "id") String authorId) {

        // Create HttpHeaders and set Content-Type, Content-Disposition headers
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Set the appropriate content type
        // headers.setContentDispositionFormData("attachment", authorId + ".jpg"); // Specify filename for download

        byte[] authorImage = authorService.getAuthorImage(authorId);
        if (authorImage != null) { // If image not retrieved through S3 Bucket, try to fetch it from Database.
            return new ResponseEntity<>(authorImage, headers, HttpStatus.OK);
        }

        final AuthorResponse response = authorService.getAuthor(authorId);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(AUTHOR_NOT_FOUND));
        }
        authorImage = response.getImage();
        return new ResponseEntity<>(authorImage, headers, HttpStatus.OK);
    }


    @PutMapping(path = "/{id}/update", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE  }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Response> updateAuthor(@UUID(message = "Author id must be a valid UUID") @PathVariable(name = "id") String authorId,
                                                @Valid @ModelAttribute("authorRequest") AuthorRequest pAuthorRequest) {
        /**
         * Check if the author is present against the id,
         * And if presents, then do update operation. Else, return 404.
         */
        final AuthorResponse authorResponse = authorService.getAuthor(authorId);
        if (authorResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(AUTHOR_NOT_FOUND));
        }

        final AuthorResponse response = authorService.updateAuthor(authorId, pAuthorRequest);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(getErrorResponse(AUTHOR_NOT_UPDATED));
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}