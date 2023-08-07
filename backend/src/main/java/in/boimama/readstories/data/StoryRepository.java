package in.boimama.readstories.data;

import in.boimama.readstories.data.model.Story;
import in.boimama.readstories.data.model.StoryPrimaryKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoryRepository extends CassandraRepository<Story, StoryPrimaryKey> {

    Story findByStoryId(final UUID storyId);

    /**
     * This method doesn't throw any error, if resource doesn't exist
     *
     * @param storyId
     */
    void deleteByStoryId(final UUID storyId);
}
