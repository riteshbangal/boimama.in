package in.boimama.readstories.data.model;

// TODO: Use Lombok to eliminate boilerplate code
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Model that represents the author table in Cassandra.
 * Stores the author information retrievable by the Author ID and Name
 */
@Table(value = "author")
public class Author {

    @PrimaryKeyColumn(name = "author_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.UUID)
    private UUID authorId;

    @Column("author_name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String authorName;

    @Column("user_name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String username;

    @Column("biography")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String biography;

    @Column("followers")
    @CassandraType(type = CassandraType.Name.INT)
    private int numberOfFollowers;

    @Column("published_works")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT) // TODO: Make this list of an object type
    private List<String> publishedWorks;

    @Column("contact_details")
    @CassandraType(type = CassandraType.Name.TEXT) // TODO: Make this an object type
    private String contactDetails;

    @Column("joining_date")
    @CassandraType(type = CassandraType.Name.DATE)
    private LocalDate joiningDate;

    /*
     * Note: Unused. Since, serving images via URL from a cloud-based storage service like AWS S3 or Azure Blob Storage
     * is considered to be faster and more scalable compared to storing images as blobs/bytes directly in the database.
     */
    @Column("author_image")
    @CassandraType(type = CassandraType.Name.BLOB)
    private byte[] image;

    @Column("author_image_path")
    @CassandraType(type = CassandraType.Name.TEXT)
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
    @CassandraType(type = CassandraType.Name.INT)
    private int version = 1;

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
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

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
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
