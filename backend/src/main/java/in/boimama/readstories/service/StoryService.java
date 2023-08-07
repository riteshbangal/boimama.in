package in.boimama.readstories.service;

import in.boimama.readstories.config.cassandra.CassandraConfig;
import in.boimama.readstories.data.StoryRepository;
import in.boimama.readstories.data.UserRepository;
import in.boimama.readstories.data.model.Story;
import in.boimama.readstories.data.model.StoryPrimaryKey;
import in.boimama.readstories.dto.StoryRequest;
import in.boimama.readstories.dto.StoryResponse;
import in.boimama.readstories.exception.ApplicationException;
import in.boimama.readstories.utils.ModelMapperHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

import static in.boimama.readstories.utils.ApplicationConstants.UNCATEGORIZED_TYPE;
import static in.boimama.readstories.utils.ApplicationUtils.estimateStoryLengthInMinutes;
import static in.boimama.readstories.utils.ApplicationUtils.getImageBytes;

@Service
public class StoryService {

    private static final Logger logger = LoggerFactory.getLogger(StoryService.class);

    @Autowired(required = true)
    private CassandraConfig cassandraConfig;

    @Autowired(required = true)
    private StoryRepository storyRepository;

    @Autowired(required = true)
    private UserRepository sampleUserRepository;

    @Autowired(required = true)
    private ModelMapperHelper modelMapperHelper;

    public StoryResponse addStory(final StoryRequest pRequest) throws ApplicationException {
        final Story story = new Story();

        story.setStoryId(UUID.randomUUID());
        story.setStoryName(pRequest.getTitle());
        story.setTitle(pRequest.getTitle());
        story.setLengthInMins(estimateStoryLengthInMinutes(pRequest.getContent()));
        story.setDescription(pRequest.getDescription());
        story.setAuthorIds(pRequest.getAuthorIds().stream().map(UUID::fromString).toList());
        story.setAuthorNames(pRequest.getAuthorNames());
        story.setRating(0); // Initially no ratings. Setting value to 0
        story.setCategory(UNCATEGORIZED_TYPE);
        story.setPublishedDate(LocalDate.now());
        story.setContent(pRequest.getContent());
        story.setImagePath("todo"); // TODO
        story.setImage(getImageBytes(pRequest.getStoryImage())); // TODO: Store it into S3 instead of database

        final StoryPrimaryKey storyPrimaryKey = new StoryPrimaryKey();
        storyPrimaryKey.setStoryId(story.getStoryId());
        storyPrimaryKey.setStoryName(story.getStoryName());
        story.setStoryPrimaryKey(storyPrimaryKey);

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
            storyRepository.findAll().forEach(storyRepository::delete);
            logger.info("Deleted all stories");
        } catch (Exception exception) {
            logger.error("Unable to delete all stories!", exception);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
