package in.boimama.readstories.data;

import in.boimama.readstories.data.model.User;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CassandraRepository<User, String> {
    User findByUsername(final String username);
}
