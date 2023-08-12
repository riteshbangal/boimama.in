package in.boimama.readstories.data;

import in.boimama.readstories.data.model.Story;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public interface StoryRepository extends CassandraRepository<Story, UUID> {

    Story findByStoryId(final UUID storyId);

    /**
     * This method doesn't throw any error, if resource doesn't exist
     *
     * @param storyId
     */
    void deleteByStoryId(final UUID storyId);

    @Query("SELECT * FROM story WHERE story_title = ?0 ALLOW FILTERING")
    List<Story> findByStoryTitle(final String storyTitle);

    @Query("SELECT * FROM story WHERE story_id = ?0 and story_title = ?1 ALLOW FILTERING")
    List<Story> findByStoryIdAndTitle(final UUID storyId, final String storyTitle);

    default List<List<UUID>> findAuthorIdsByStoryTitle(final String storyTitle) {
        return findByStoryTitle(storyTitle).stream()
                .map(Story::getAuthorIds)
                .collect(Collectors.toList());
    }

    /**
     * It takes a story title and a list of author IDs as inputs
     * and checks whether the combination of the story title and author IDs already exists.
     *
     * @param pStoryTitle
     * @param pAuthorIds
     * @return result of uniqueness check
     */
    default boolean isStoryWithSameAuthorsAlreadyExists(final String pStoryTitle, final List<UUID> pAuthorIds) {
        /**
         * This use case needs to validate if the list/set contains the same elements but in a different order.
         * The equals method for sets considers two sets to be equal if they contain the same elements, regardless of their order.
         */
        return findByStoryTitle(pStoryTitle).stream()
                .anyMatch(story -> new HashSet<>(story.getAuthorIds()).equals(new HashSet<>(pAuthorIds)));
    }
}
