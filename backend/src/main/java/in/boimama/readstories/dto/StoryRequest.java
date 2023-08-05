package in.boimama.readstories.dto;

import io.swagger.v3.oas.annotations.media.Schema;
//import javax.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Schema(requiredProperties = { "content" })
public class StoryRequest {

    @Schema(description = "Title of the story", required = true, maxLength = 5)
    private String title;

    private String category;

    @Schema(description = "Description of the story", nullable = false)
    private String description;

    @Schema(description = "Content of the story", required = true)
    private String content;
    private LocalDate publishedDate;
    private List<UUID> authorIds;
    private List<String> authorNames;
    private int rating;
    private int lengthInMins;
    private String imagePath;

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

    public List<UUID> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(List<UUID> authorIds) {
        this.authorIds = authorIds;
    }

    public List<String> getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getLengthInMins() {
        return lengthInMins;
    }

    public void setLengthInMins(int lengthInMins) {
        this.lengthInMins = lengthInMins;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
                ", rating=" + rating +
                ", lengthInMins=" + lengthInMins +
                ", imagePath=" + imagePath +
                '}';
    }
}
