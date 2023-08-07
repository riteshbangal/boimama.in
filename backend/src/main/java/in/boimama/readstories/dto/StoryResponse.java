package in.boimama.readstories.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

// TODO: Use Lombok to eliminate boilerplate code
public class StoryResponse implements Response {

    private UUID id;
    private String title;
    private String category;
    private String description;
    private String content;
    private LocalDate publishedDate;
    private List<UUID> authorIds;
    private List<String> authorNames;
    private int rating;
    private int lengthInMins;
    private String imagePath;
    @JsonIgnore
    private byte[] image;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "StoryResponse{" +
                "id=" + id +
                ", title='" + title + '\'' +
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
