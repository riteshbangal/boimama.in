package in.boimama.readstories.service;

import com.datastax.oss.driver.api.core.CqlSession;
import in.boimama.readstories.config.cassandra.CassandraConfig;
import in.boimama.readstories.data.StoryRepository;
import in.boimama.readstories.data.model.Story;
import in.boimama.readstories.data.model.StoryPrimaryKey;
import in.boimama.readstories.data.model.User;
import in.boimama.readstories.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class StoryService {

    @Autowired(required = true)
    private CassandraConfig cassandraConfig;

    @Autowired(required = true)
    private StoryRepository storyRepository;

    @Autowired(required = true)
    private UserRepository sampleUserRepository;

    public Story getStory (String storyId, String storyTitle) {
        StoryPrimaryKey storyPrimaryKey = new StoryPrimaryKey();
        storyPrimaryKey.setStoryName(storyTitle);
        storyPrimaryKey.setStoryId(UUID.fromString(storyId));

        return storyRepository.findById(storyPrimaryKey).get();
        //return storyRepository.findByTitle(storyTitle);
    }

    public Story getStoryByTitle (String storyTitle) {
        StoryPrimaryKey storyPrimaryKey = new StoryPrimaryKey();
        storyPrimaryKey.setStoryName(storyTitle);
        storyPrimaryKey.setStoryId(UUID.fromString("d198898e-6aaa-4205-ab66-2a5da9794797"));

        return storyRepository.findById(storyPrimaryKey).get();
        //return storyRepository.findByTitle(storyTitle);
    }

    public void addStory() throws IOException {

        Story story = new Story();

        story.setStoryId(UUID.randomUUID());
        story.setStoryName("My First Story");
        story.setTitle(story.getStoryName());
        story.setLengthInMins(15);
        story.setDescription("story description");
        story.setAuthorIds(List.copyOf(Arrays.asList(UUID.randomUUID())));
        story.setAuthorNames(List.copyOf(Arrays.asList("Ritesh")));
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

    private static byte[] getImageBytes() throws IOException {
        File file = new File(System.getProperty("user.dir") + "/src/main/resources/author.jpg");
        byte[] bytes = new byte[(int) file.length()];
        FileInputStream inputStream = new FileInputStream(file);
        inputStream.read(bytes);
        inputStream.close();
        return bytes;
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
