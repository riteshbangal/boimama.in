package in.boimama.readstories.service;

import com.datastax.oss.driver.api.core.CqlSession;
import in.boimama.readstories.config.cassandra.CassandraConfig;
import in.boimama.readstories.data.StoryRepository;
import in.boimama.readstories.data.UserRepository;
import in.boimama.readstories.data.model.Story;
import in.boimama.readstories.data.model.StoryPrimaryKey;
import in.boimama.readstories.data.model.User;
import in.boimama.readstories.dto.StoryResponse;
import in.boimama.readstories.utils.ModelMapperHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

    private static byte[] getImageBytes() throws IOException {
        File file = new File(System.getProperty("user.dir") + "/src/main/resources/author.jpg");
        byte[] bytes = new byte[(int) file.length()];
        FileInputStream inputStream = new FileInputStream(file);
        inputStream.read(bytes);
        inputStream.close();
        return bytes;
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

    public Story getStory(String storyId, String storyName) {
        StoryPrimaryKey storyPrimaryKey = new StoryPrimaryKey();
        storyPrimaryKey.setStoryName(storyName);
        storyPrimaryKey.setStoryId(UUID.fromString(storyId));

        return storyRepository.findById(storyPrimaryKey).get();
    }

    public Story getStoryByTitle(String storyTitle) {
        StoryPrimaryKey storyPrimaryKey = new StoryPrimaryKey();
        storyPrimaryKey.setStoryName(storyTitle);
        storyPrimaryKey.setStoryId(UUID.fromString("d198898e-6aaa-4205-ab66-2a5da9794797"));

        //return storyRepository.findById(storyPrimaryKey).get();
        return storyRepository.findByTitle(storyTitle);
    }

    public void addStory() throws IOException {

        Story story = new Story();

        story.setStoryId(UUID.randomUUID());
        story.setStoryName("My First Story");
        story.setTitle(story.getStoryName());
        story.setLengthInMins(15);
        story.setDescription("story description");
        story.setAuthorIds(List.copyOf(List.of(UUID.randomUUID())));
        story.setAuthorNames(List.copyOf(List.of("Ritesh")));
        story.setRating(4);
        story.setCategory("Comedy");
        story.setPublishedDate(LocalDate.now());
        story.setContent("বিজয়টা jet যে এত বড় চোর; না না বলা ভালো, ছ্যাঁচোড় হবে; সেটা স্বপ্নেও ভাবি নি। আমারই বংশের রক্ত, যে ওর শরীরেও বইছে; এ কথা ভাবলেই ঘেন্না হয়। আমার স্ত্রী, গত-দশ বছর ধরে ধরে, খুটিনাটি বিষয় বিষয়");
        story.setImage(getImageBytes());

        StoryPrimaryKey storyPrimaryKey = new StoryPrimaryKey();
        storyPrimaryKey.setStoryId(story.getStoryId());
        storyPrimaryKey.setStoryName(story.getStoryName());
        story.setStoryPrimaryKey(storyPrimaryKey);

        storyRepository.insert(story);
    }

    public void addUser(String user) {
        String userName = user + "-user";

        User userIn = new User();
        userIn.setUsername(userName);
        userIn.setFname(user);
        userIn.setLname("Gupta");

        sampleUserRepository.insert(userIn);

        User userOut = sampleUserRepository.findByUsername(userName);

        System.out.println("Primary Key: " + userOut.getUsername());
    }

    public String getCount() {
        CqlSession cqlSession = null;
        try {
            cqlSession = cassandraConfig.session();
            int count = cqlSession.execute("SELECT * FROM boimama.user").all().size();

            System.out.println("Number of hosts: " + count);
            return "Number of hosts: " + count;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
