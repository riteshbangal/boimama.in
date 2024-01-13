package in.boimama.readstories.service;

import in.boimama.readstories.config.cassandra.CassandraConfig;
import in.boimama.readstories.data.StoryRepository;
import in.boimama.readstories.data.model.Story;
import in.boimama.readstories.dto.StoryRequest;
import in.boimama.readstories.dto.StoryResponse;
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
import java.util.stream.Collectors;

import static in.boimama.readstories.utils.ApplicationConstants.UNCATEGORIZED_TYPE;
import static in.boimama.readstories.utils.ApplicationUtils.estimateStoryLengthInMinutes;
import static in.boimama.readstories.utils.ApplicationUtils.getFileBytes;
import static in.boimama.readstories.utils.ApplicationUtils.isEmpty;

@Service
public class StoryService {

    private static final Logger logger = LoggerFactory.getLogger(StoryService.class);

    public static final String STORY_COVER_IMAGE_DIRECTORY_NAME = "story-cover-images";

    @Autowired(required = true)
    private CassandraConfig cassandraConfig;

    @Autowired(required = true)
    private StoryRepository storyRepository;

    @Autowired(required = false)
    private AwsImageManager awsImageManager;

    @Autowired(required = true)
    private ModelMapperHelper modelMapperHelper;

    public StoryResponse addStory(final StoryRequest pRequest,
                                  final String pServerPath,
                                  final String pContextPath) throws ApplicationServerException {

        if (storyRepository.isStoryWithSameAuthorsAlreadyExists(pRequest.getTitle(),
                pRequest.getAuthorIds().stream().map(UUID::fromString).toList())) {
            logger.error("Story with same title ({}) and authors {} already exists",
                    pRequest.getTitle(), pRequest.getAuthorIds());
            throw new ApplicationClientException("Story with same title and authors already exists");
        }

        final Story story = new Story();
        final UUID storyId = UUID.randomUUID();

        story.setStoryId(storyId);
        story.setStoryTitle(pRequest.getTitle());
        story.setLengthInMins(estimateStoryLengthInMinutes(pRequest.getContent()));
        story.setDescription(pRequest.getDescription());
        story.setAuthorIds(pRequest.getAuthorIds().stream().map(UUID::fromString).toList());
        story.setAuthorNames(pRequest.getAuthorNames());
        story.setRating(0); // Initially no ratings. Setting value to 0
        story.setCategory(isEmpty(pRequest.getCategory()) ? UNCATEGORIZED_TYPE : pRequest.getCategory());
        story.setPublishedDate(LocalDate.now());
        story.setContent(pRequest.getContent());
        story.setImagePath(pServerPath + pContextPath + "/story/" + storyId + "/image");

        // Store image into Database
        story.setImage(getFileBytes(pRequest.getStoryImage()));
        // Store image into S3 Bucket
        awsImageManager.uploadImage(STORY_COVER_IMAGE_DIRECTORY_NAME, storyId.toString(), pRequest.getStoryImage());

        storyRepository.insert(story);
        return modelMapperHelper.mapStory(story, StoryResponse.class);
    }

    public StoryResponse getStory(String storyId) {
        final Story story = storyRepository.findByStoryId(UUID.fromString(storyId));
        if (story == null) {
            logger.warn("Story not found against id: {}", storyId);
            return null;
        }

        logger.debug("Story found against id: {}", storyId);
        return modelMapperHelper.mapStory(story, StoryResponse.class);
    }

    public byte[] getStoryImage(String storyId) {
        final File imageFile = awsImageManager.getImage(STORY_COVER_IMAGE_DIRECTORY_NAME + "/" + storyId);
        if (imageFile == null) {
            logger.warn("Image not found into AWS S3 Bucket against id: {}", storyId);
            return null;
        }

        logger.debug("Image found into AWS S3 Bucket against id: {}", storyId);
        return getFileBytes(imageFile);
    }

    public List<StoryResponse> getAllStories() {
        final List<StoryResponse> storiesResponse = new ArrayList<>();
        storyRepository.findAll()
                .forEach(story -> storiesResponse.add(modelMapperHelper.mapStory(story, StoryResponse.class)));
        if (isEmpty(storiesResponse))
            logger.warn("No story found!");
        return storiesResponse;
    }

    public List<StoryResponse> searchStories(final String pSearchText) {
        /**
         * This is not a recommended practice!
         * AWS Keyspace (Cassandra) doesn't support operator LIKE.
         * TODO: Hence we need some better integrated solution for searching.
         * For now do it via Java coding! Get all stories and match them with the search keyword!
         */
        final List<StoryResponse> storiesResponse = storyRepository.findAll().stream()
                .filter(story -> story.getStoryTitle().toLowerCase().contains(pSearchText.toLowerCase())
                        || story.getDescription().toLowerCase().contains(pSearchText.toLowerCase())
                        || story.getContent().toLowerCase().contains(pSearchText.toLowerCase())
                        || story.getAuthorNames().stream()
                        .anyMatch(authorName -> authorName.toLowerCase().contains(pSearchText.toLowerCase()))
                        || story.getCategory().toLowerCase().contains(pSearchText.toLowerCase())
                )
                .map(story -> modelMapperHelper.mapStory(story, StoryResponse.class))
                .collect(Collectors.toList());
        if (isEmpty(storiesResponse))
            logger.warn("No story found!");
        return storiesResponse;
    }

    public List<StoryResponse> searchStoriesByCategory(final String pStoryCategory) {
        final List<StoryResponse> storiesResponse = new ArrayList<>();
        /**
         * This is not a recommended practice!
         * AWS Keyspace (Cassandra) doesn't support operator LIKE.
         * TODO: Hence we need some better integrated solution for searching.
         * For now do it via Java coding! Get all stories and match them with the search keyword!
         */
        storyRepository.findAll().stream()
                .filter(story -> story.getCategory().toLowerCase().contains(pStoryCategory.toLowerCase()))
                .forEach(story -> storiesResponse.add(modelMapperHelper.mapStory(story, StoryResponse.class)));
        /**
         * AWS Keyspace (Cassandra) doesn't support operator LIKE.
         * And due to case-sensitive search, this is not working as expected!
         */
        /*
        storyRepository.findByStoryCategory(pStoryCategory)
                .forEach(story -> storiesResponse.add(modelMapperHelper.mapStory(story, StoryResponse.class)));
        */
        if (isEmpty(storiesResponse))
            logger.warn("No story found!");
        return storiesResponse;
    }

    public boolean deleteStory(String storyId) {
        try {
            logger.debug("Story found against id: {}", storyId);
            storyRepository.deleteByStoryId(UUID.fromString(storyId)); // Doesn't throw any error, if resource doesn't exist!
            logger.debug("Deleted story with id: {}", storyId);
        } catch (Exception exception) {
            logger.error("Unable to delete story with id: {}", storyId, exception);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public boolean deleteAllStories() {
        try {
            logger.info("Deleting all stories");
            // storyRepository.deleteAll(); // CassandraInvalidQueryException: Query; CQL [TRUNCATE story];
            storyRepository.findAll().forEach(storyRepository::delete);  // ArrayIndexOutOfBoundsException: Index 12 out of bounds for length 12 - when duplicate ids present
            logger.info("Deleted all stories");
        } catch (Exception exception) {
            logger.error("Unable to delete all stories!", exception);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public StoryResponse updateStory(final String storyId,
                                     final StoryRequest pRequest) throws ApplicationServerException {
        final Story story = storyRepository.findByStoryId(UUID.fromString(storyId));
        if (story == null) {
            logger.error("Story not found against id: {}", storyId);
            throw new ApplicationServerException("Story not found against id: " + storyId);
        }

        logger.debug("Story to be update have id: {}", storyId);
        if (storyRepository.isStoryWithSameAuthorsAlreadyExists(pRequest.getTitle(),
                pRequest.getAuthorIds().stream().map(UUID::fromString).toList())) {
            logger.debug("Story: {} with same title ({}) and authors {} already exists",
                    storyId, pRequest.getTitle(), pRequest.getAuthorIds());
        } else {
            story.setStoryTitle(pRequest.getTitle());
        }
        story.setLengthInMins(estimateStoryLengthInMinutes(pRequest.getContent()));
        story.setDescription(pRequest.getDescription());
        story.setAuthorIds(pRequest.getAuthorIds().stream().map(UUID::fromString).toList());
        story.setAuthorNames(pRequest.getAuthorNames());
        story.setCategory(isEmpty(pRequest.getCategory()) ?
                (isEmpty(story.getCategory()) ? UNCATEGORIZED_TYPE : story.getCategory()) : pRequest.getCategory());
        story.setPublishedDate(pRequest.getPublishedDate() == null ? story.getPublishedDate() : pRequest.getPublishedDate());
        story.setContent(pRequest.getContent());
        story.setImage(getFileBytes(pRequest.getStoryImage()));

        storyRepository.save(story);
        return modelMapperHelper.mapStory(story, StoryResponse.class);
    }

}
