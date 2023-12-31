package in.boimama.readstories.data.model;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

// TODO: Use Lombok to eliminate boilerplate code
/**
 * Model that represents the story table in Cassandra.
 * Stores the story information retrievable by the story ID and Name
 */
@Table(value = "story")
public class Story {

    @PrimaryKeyColumn(name = "story_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = Name.UUID)
    private UUID storyId;

    @Column("story_title")
    @CassandraType(type = Name.TEXT)
    private String storyTitle;

    @Column("story_category")
    @CassandraType(type = Name.TEXT)
    private String category;

    @Column("story_description")
    @CassandraType(type = Name.TEXT)
    private String description;

    @Column("story_content")
    @CassandraType(type = Name.TEXT)
    private String content;

    @Column("published_date")
    @CassandraType(type = Name.DATE)
    private LocalDate publishedDate;

    @Column("author_ids")
    @CassandraType(type = Name.LIST, typeArguments = Name.UUID)
    private List<UUID> authorIds;

    @Column("author_names")
    @CassandraType(type = Name.LIST, typeArguments = Name.TEXT)
    private List<String> authorNames;

    @Column("story_rating")
    @CassandraType(type = Name.INT)
    private int rating;

    @Column("story_length_in_mins")
    @CassandraType(type = Name.INT)
    private int lengthInMins;

    /*
     * Note: Unused. Since, serving images via URL from a cloud-based storage service like AWS S3 or Azure Blob Storage
     * is considered to be faster and more scalable compared to storing images as blobs/bytes directly in the database.
     */
    @Column("story_image")
    @CassandraType(type = Name.BLOB)
    private byte[] image;

    @Column("story_image_path")
    @CassandraType(type = Name.TEXT)
    private String imagePath;

    /**
     * This is used as a Clustering column.
     * Use of clustering columns to sort rows within a partition key to improve the performance of your queries.
     *
     * A clustering column that is part of your primary key,
     * it could potentially lead to issues with updating or overwriting existing records.
     * Clustering columns determine the physical order of data within a partition,
     * and updates to clustering columns can result in new records being inserted rather than updating the existing ones.
     *
     * Important Note: As per current design this column will be always static,
     * and it is being introduced to avoid above issue.
     */
    @Column("version")
    @CassandraType(type = Name.INT)
    private int version = 1;

    public UUID getStoryId() {
        return storyId;
    }

    public void setStoryId(UUID storyId) {
        this.storyId = storyId;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
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

    public List<String> getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }

    public List<UUID> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(List<UUID> authorIds) {
        this.authorIds = authorIds;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
