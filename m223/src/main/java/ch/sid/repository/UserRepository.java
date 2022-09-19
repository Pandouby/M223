package ch.sid.repository;

import ch.sid.model.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
