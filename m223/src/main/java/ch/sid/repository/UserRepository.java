package ch.sid.repository;

import ch.sid.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Long, User> {
}
