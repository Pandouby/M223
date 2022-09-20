package ch.sid.repository;

import ch.sid.model.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends CrudRepository<Member, UUID> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndPassword(String email, String password);
}
