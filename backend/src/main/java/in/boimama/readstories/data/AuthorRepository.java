package in.boimama.readstories.data;

import in.boimama.readstories.data.model.Author;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static in.boimama.readstories.utils.ApplicationUtils.isEmpty;

@Repository
public interface AuthorRepository extends CassandraRepository<Author, UUID> {

    Author findByAuthorId(final UUID authorId);

    /**
     * This method doesn't throw any error, if resource doesn't exist
     *
     * @param authorId
     */
    void deleteByAuthorId(final UUID authorId);

    @Query("SELECT * FROM author WHERE author_name = ?0 ALLOW FILTERING")
    List<Author> findByAuthorName(final String authorName);

    @Query("SELECT * FROM author WHERE user_name = ?0 ALLOW FILTERING")
    List<Author> findByAuthorUsername(final String authorUsername);

    @Query("SELECT * FROM author WHERE author_id = ?0 and author_name = ?1 ALLOW FILTERING")
    List<Author> findByAuthorIdAndName(final UUID authorId, final String authorName);

    /**
     * It takes a author's User-Name/ID as inputs and checks whether the author already exists.
     *
     * @param pAuthorUsername
     * @return result of uniqueness check
     */
    default boolean isAuthorAlreadyExists(final String pAuthorUsername) {
        /**
         * This use case needs to validate if the list/set contains the same elements but in a different order.
         * The equals method for sets considers two sets to be equal if they contain the same elements, regardless of their order.
         */
        List<Author> authorList = findByAuthorUsername(pAuthorUsername);
        return !isEmpty(authorList);
    }
}
