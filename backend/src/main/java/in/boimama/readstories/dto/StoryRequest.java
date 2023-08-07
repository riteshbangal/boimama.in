package in.boimama.readstories.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.boimama.readstories.utils.validation.NonEmptyStringList;
import in.boimama.readstories.utils.validation.UuidList;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public class StoryRequest {

    @Schema(description = "Title of the story", required = true, minLength = 5, maxLength = 50)
    @NotNull(message = "'title' is missing in request body")
    @Size(min=5, message = "'title' should be at least 5 character")
    @Size(max=50, message = "'title' should not be greater than 50 characters")
    private String title;

    @Schema(description = "Category of the story. By default it's not categorised")
    private String category;

    @Schema(description = "Description of the story", required = true, minLength = 10, maxLength = 500)
    @NotNull(message = "'description' is missing in request body")
    @Size(min=10, message = "'description' should be at least 10 character")
    @Size(max=500, message = "'description' should not be greater than 500 characters")
    private String description;

    @Schema(description = "Content of the story")
    @NotNull(message = "'content' is missing in request body")
    @Size(min=10, message = "'description' should be at least 10 character")
    @Size(max=500, message = "'description' should not be greater than 500 characters")
    private String content;

    @Schema(description = "Published date of the story. In case of empty input, it will pick current date")
    private LocalDate publishedDate;

    @Schema(description = "Author's id(s) of the story", required = true)
    @NotNull(message = "Author id(s) must not be null")
    @NotEmpty(message = "Author id(s) must not be empty")
    @UuidList(message = "Author id list must contain valid UUID(s)")
    private List<String> authorIds;

    @Schema(description = "Author's name(s) of the story", required = true)
    @NotNull(message = "Author name(s) must not be null")
    @NotEmpty(message = "Author name(s) must not be empty")
    @NonEmptyStringList(message = "Author name(s) must be non-empty strings, with 2-50 characters", minLength = 2, maxLength = 50)
    private List<String> authorNames;

    @JsonIgnore
    @Schema(description = "Cover image of the story")
    private MultipartFile storyImage;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public List<String> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(List<String> authorIds) {
        this.authorIds = authorIds;
    }

    public List<String> getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }

    public MultipartFile getStoryImage() {
        return storyImage;
    }

    public void setStoryImage(MultipartFile storyImage) {
        this.storyImage = storyImage;
    }

    @Override
    public String toString() {
        return "StoryResponse{" +
                "title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", publishedDate=" + publishedDate +
                ", authorIds=" + authorIds +
                ", authorNames=" + authorNames +
                '}';
    }
}
