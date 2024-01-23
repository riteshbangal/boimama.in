package in.boimama.readstories.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.boimama.readstories.utils.validation.NonEmptyStringList;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Component
@RequestScope
public class AuthorRequest {

    @Schema(description = "Name of the author", required = true, minLength = 5, maxLength = 50)
    @NotNull(message = "'name' is missing in request body")
    @Size(min=5, message = "'name' should be at least 5 character")
    @Size(max=50, message = "'name' should not be greater than 50 characters")
    private String name;

    @Schema(description = "User Id/Name of the author", required = true, minLength = 3, maxLength = 50)
    @NotNull(message = "'username' is missing in request body")
    @Size(min=3, message = "'username' should be at least 3 character")
    @Size(max=50, message = "'username' should not be greater than 50 characters")
    private String username;

    @Schema(description = "Biography of the author", required = true, minLength = 10, maxLength = 5000)
    @NotNull(message = "'biography' is missing in request body")
    @Size(min=10, message = "'biography' should be at least 10 character")
    @Size(max=5000, message = "'biography' should not be greater than 5000 characters")
    private String biography;

    @Schema(description = "Number of followers of the author")
    @Positive(message = "'numberOfFollowers' should be grater than or equals to Zero")
    private int numberOfFollowers;

    @Schema(description = "Joining date of the author. In case of empty input, it will pick current date")
    private LocalDate joiningDate;

    @JsonIgnore
    @Schema(description = "Cover image of the author")
    private MultipartFile authorImage;

    @Schema(description = "Published work(s) of the author", required = false)
    @NonEmptyStringList(message = "Published work(s) must be non-empty strings, with 2-500 characters", minLength = 2, maxLength = 500)
    private List<String> publishedWorks;

    @Schema(description = "Contact details of the author", required = false, minLength = 10, maxLength = 500)
    private String contactDetails; // TODO: Make this an object type

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public int getNumberOfFollowers() {
        return numberOfFollowers;
    }

    public void setNumberOfFollowers(int numberOfFollowers) {
        this.numberOfFollowers = numberOfFollowers;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public MultipartFile getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(MultipartFile authorImage) {
        this.authorImage = authorImage;
    }

    public List<String> getPublishedWorks() {
        return publishedWorks;
    }

    public void setPublishedWorks(List<String> publishedWorks) {
        this.publishedWorks = publishedWorks;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    @Override
    public String toString() {
        return "AuthorRequest{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", biography='" + biography + '\'' +
                ", numberOfFollowers=" + numberOfFollowers +
                ", joiningDate=" + joiningDate +
                ", authorImage=" + authorImage +
                ", publishedWorks='" + publishedWorks + '\'' +
                ", contactDetails='" + contactDetails + '\'' +
                '}';
    }
}
