package in.boimama.readstories.utils;

import in.boimama.readstories.data.model.Author;
import in.boimama.readstories.data.model.Story;
import in.boimama.readstories.dto.AuthorResponse;
import in.boimama.readstories.dto.StoryResponse;
import in.boimama.readstories.exception.ApplicationServerException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelMapperHelperTest {

    private final ModelMapperHelper modelMapperHelper = new ModelMapperHelper();

    @Test
    void testMapAuthor() throws ApplicationServerException {
        // Test data
        Author authorDAO = new Author();
        authorDAO.setAuthorId(UUID.randomUUID());
        authorDAO.setAuthorName("John Doe");
        authorDAO.setUsername("johndoe");
        authorDAO.setBiography("A talented author");
        authorDAO.setNumberOfFollowers(100);
        authorDAO.setJoiningDate(LocalDate.now());
        authorDAO.setContactDetails("contact@example.com");
        authorDAO.setPublishedWorks(Arrays.asList("First mock work", "Second mock work"));
        authorDAO.setImagePath("/images/johndoe.jpg");
        authorDAO.setImage(new byte[]{1, 2, 3});

        // Test
        AuthorResponse authorResponse = modelMapperHelper.mapAuthor(authorDAO, AuthorResponse.class);

        // Assertions
        assertEquals(authorDAO.getAuthorId(), authorResponse.getId());
        assertEquals(authorDAO.getAuthorName(), authorResponse.getName());
        assertEquals(authorDAO.getUsername(), authorResponse.getUsername());
        assertEquals(authorDAO.getBiography(), authorResponse.getBiography());
        assertEquals(authorDAO.getNumberOfFollowers(), authorResponse.getNumberOfFollowers());
        assertEquals(authorDAO.getJoiningDate(), authorResponse.getJoiningDate());
        assertEquals(authorDAO.getContactDetails(), authorResponse.getContactDetails());
        assertEquals(authorDAO.getPublishedWorks(), authorResponse.getPublishedWorks());
        assertEquals(authorDAO.getImagePath(), authorResponse.getImagePath());
        assertEquals(authorDAO.getImage(), authorResponse.getImage());
    }

    @Test
    void testMapStory() throws ApplicationServerException {
        // Test data
        Story storyDAO = new Story();
        storyDAO.setStoryId(UUID.randomUUID());
        storyDAO.setStoryTitle("Sample Story");
        storyDAO.setCategory("Fiction");
        storyDAO.setDescription("A short story");
        storyDAO.setContent("Once upon a time...");
        storyDAO.setPublishedDate(LocalDate.now());
        storyDAO.setAuthorIds(Arrays.asList(UUID.randomUUID(), UUID.randomUUID()));
        storyDAO.setAuthorNames(Arrays.asList("Author1", "Author2"));
        storyDAO.setRating(4);
        storyDAO.setLengthInMins(10);
        storyDAO.setImagePath("/images/sample-story.jpg");
        storyDAO.setImage(new byte[]{4, 5, 6});

        // Test
        StoryResponse storyResponse = modelMapperHelper.mapStory(storyDAO, StoryResponse.class);

        // Assertions
        assertEquals(storyDAO.getStoryId(), storyResponse.getId());
        assertEquals(storyDAO.getStoryTitle(), storyResponse.getTitle());
        assertEquals(storyDAO.getCategory(), storyResponse.getCategory());
        assertEquals(storyDAO.getDescription(), storyResponse.getDescription());
        assertEquals(storyDAO.getContent(), storyResponse.getContent());
        assertEquals(storyDAO.getPublishedDate(), storyResponse.getPublishedDate());
        assertEquals(storyDAO.getAuthorIds(), storyResponse.getAuthorIds());
        assertEquals(storyDAO.getAuthorNames(), storyResponse.getAuthorNames());
        assertEquals(storyDAO.getRating(), storyResponse.getRating());
        assertEquals(storyDAO.getLengthInMins(), storyResponse.getLengthInMins());
        assertEquals(storyDAO.getImagePath(), storyResponse.getImagePath());
        assertEquals(storyDAO.getImage(), storyResponse.getImage());
    }
}
