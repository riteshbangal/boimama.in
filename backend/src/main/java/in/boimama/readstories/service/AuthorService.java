package in.boimama.readstories.service;

import in.boimama.readstories.config.cassandra.CassandraConfig;
import in.boimama.readstories.data.AuthorRepository;
import in.boimama.readstories.data.model.Author;
import in.boimama.readstories.dto.AuthorRequest;
import in.boimama.readstories.dto.AuthorResponse;
import in.boimama.readstories.exception.ApplicationClientException;
import in.boimama.readstories.exception.ApplicationServerException;
import in.boimama.readstories.utils.AwsImageManager;
import in.boimama.readstories.utils.ModelMapperHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static in.boimama.readstories.utils.ApplicationUtils.getFileBytes;
import static in.boimama.readstories.utils.ApplicationUtils.isEmpty;

@Service
public class AuthorService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    public static final String AUTHOR_COVER_IMAGE_DIRECTORY_NAME = "author-cover-images";

    @Autowired(required = true)
    private CassandraConfig cassandraConfig;

    @Autowired(required = true)
    private AuthorRepository authorRepository;

    @Autowired(required = false)
    private AwsImageManager awsImageManager;

    @Autowired(required = true)
    private ModelMapperHelper modelMapperHelper;

    public AuthorResponse addAuthor(final AuthorRequest pRequest) throws ApplicationServerException {

        if (authorRepository.isAuthorAlreadyExists(pRequest.getUsername())) {
            logger.error("Author with same username ({}) already exists", pRequest.getUsername());
            throw new ApplicationClientException("Author with same username already exists");
        }

        final Author author = new Author();
        final UUID authorId = UUID.randomUUID();

        author.setAuthorId(authorId);
        author.setAuthorName(pRequest.getName());
        author.setUsername(pRequest.getUsername());
        author.setBiography(pRequest.getBiography());
        author.setNumberOfFollowers(pRequest.getNumberOfFollowers());
        author.setPublishedWorks(pRequest.getPublishedWorks());
        author.setContactDetails(pRequest.getContactDetails());
        author.setJoiningDate(pRequest.getJoiningDate() == null ? LocalDate.now() : pRequest.getJoiningDate());
        author.setImagePath("/author/" + authorId + "/image");

        // Store image into Database
        author.setImage(getFileBytes(pRequest.getAuthorImage()));
        // Store image into S3 Bucket
        awsImageManager.uploadImage(AUTHOR_COVER_IMAGE_DIRECTORY_NAME, authorId.toString(), pRequest.getAuthorImage());

        authorRepository.insert(author);
        return modelMapperHelper.mapAuthor(author, AuthorResponse.class);
    }

    public AuthorResponse getAuthor(String authorId) {
        final Author author = authorRepository.findByAuthorId(UUID.fromString(authorId));
        if (author == null) {
            logger.warn("Author not found against id: {}", authorId);
            return null;
        }

        logger.debug("Author found against id: {}", authorId);
        return modelMapperHelper.mapAuthor(author, AuthorResponse.class);
    }

    public byte[] getAuthorImage(String authorId) {
        final File imageFile = awsImageManager.getImage(AUTHOR_COVER_IMAGE_DIRECTORY_NAME + "/" + authorId);
        if (imageFile == null) {
            logger.warn("Image not found into AWS S3 Bucket against id: {}", authorId);
            return null;
        }

        logger.debug("Image found into AWS S3 Bucket against id: {}", authorId);
        return getFileBytes(imageFile);
    }

    public List<AuthorResponse> getAllAuthors() {
        final List<AuthorResponse> authorResponseList = new ArrayList<>();
        authorRepository.findAll()
                .forEach(author -> authorResponseList.add(modelMapperHelper.mapAuthor(author, AuthorResponse.class)));
        if (isEmpty(authorResponseList))
            logger.warn("No author found!");
        return authorResponseList;
    }

    public boolean deleteAuthor(String authorId) {
        try {
            logger.debug("Author found against id: {}", authorId);
            authorRepository.deleteByAuthorId(UUID.fromString(authorId)); // Doesn't throw any error, if resource doesn't exist!
            logger.debug("Deleted author with id: {}", authorId);
        } catch (Exception exception) {
            logger.error("Unable to delete author with id: {}", authorId, exception);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public boolean deleteAllAuthors() {
        try {
            logger.info("Deleting all authors");
            // authorRepository.deleteAll(); // CassandraInvalidQueryException: Query; CQL [TRUNCATE author];
            authorRepository.findAll().forEach(authorRepository::delete);  // ArrayIndexOutOfBoundsException: Index 12 out of bounds for length 12 - when duplicate ids present
            logger.info("Deleted all authors");
        } catch (Exception exception) {
            logger.error("Unable to delete all authors!", exception);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public AuthorResponse updateAuthor(final String authorId,
                                     final AuthorRequest pRequest) throws ApplicationServerException {
        final Author author = authorRepository.findByAuthorId(UUID.fromString(authorId));
        if (author == null) {
            logger.error("Author not found against id: {}", authorId);
            throw new ApplicationClientException("Author not found against id: " + authorId);
        }

        logger.debug("Author to be updated, have id: {} and username: {}", authorId, author.getUsername());

        // Update of username is forbidden! Restricting this.
        if (!author.getUsername().equals(pRequest.getUsername())) {
            logger.error("We're sorry, but changing your username is not allowed. " +
                            "Username is a unique identifier associated with Author: {}", authorId);
            throw new ApplicationClientException("Username update not allowed");
        }

        author.setAuthorName(pRequest.getName());
        author.setBiography(pRequest.getBiography());
        author.setNumberOfFollowers(pRequest.getNumberOfFollowers());
        author.setPublishedWorks(pRequest.getPublishedWorks());
        author.setContactDetails(pRequest.getContactDetails());
        author.setJoiningDate(pRequest.getJoiningDate() == null ? author.getJoiningDate() : pRequest.getJoiningDate());

        // Store image into Database.
        if (getFileBytes(pRequest.getAuthorImage()).length != 0) {
            author.setImage(getFileBytes(pRequest.getAuthorImage()));
        }

        authorRepository.save(author);
        return modelMapperHelper.mapAuthor(author, AuthorResponse.class);
    }
}
