package in.boimama.readstories.data;

import in.boimama.readstories.data.model.Story;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StoryRepository extends CassandraRepository<Story, UUID> {

    Story findByStoryId(final UUID storyId);

    @Query("SELECT * FROM story WHERE story_title = ?0 ALLOW FILTERING")
    List<Story> findByStoryTitle(String storyTitle);

    /**
     * This method doesn't throw any error, if resource doesn't exist
     *
     * @param storyId
     */
    void deleteByStoryId(final UUID storyId);
}
