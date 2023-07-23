package in.boimama.readstories.data;

import in.boimama.readstories.data.model.Story;
import in.boimama.readstories.data.model.StoryPrimaryKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends CassandraRepository<Story, StoryPrimaryKey> {

    Story findByTitle(final String title);
}
