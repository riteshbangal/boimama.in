package in.boimama.readstories.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

// TODO: Use Lombok to eliminate boilerplate code
public class AuthorResponse implements Response {

    @NotNull(message = "'author id' is can not be null in response body")
    private UUID id;

    private String name;
    private String username;
    private String biography;
    private int numberOfFollowers;
    private LocalDate joiningDate;
    private String contactDetails;
    private List<String> publishedWorks;
    private String imagePath;
    @JsonIgnore
    private byte[] image;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public List<String> getPublishedWorks() {
        return publishedWorks;
    }

    public void setPublishedWorks(List<String> publishedWorks) {
        this.publishedWorks = publishedWorks;
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
        return "AuthorResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", biography='" + biography + '\'' +
                ", numberOfFollowers=" + numberOfFollowers +
                ", joiningDate=" + joiningDate +
                ", contactDetails='" + contactDetails + '\'' +
                ", publishedWorks=" + publishedWorks +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
