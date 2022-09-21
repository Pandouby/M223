package ch.sid.repository;

import ch.sid.model.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Repository
public interface BookingRepository extends CrudRepository<Booking, UUID> {
    Optional<Booking> findByCreatorId(UUID id);

    List<Booking> findAllByCreatorId(UUID id);

    List<Booking> findAllByStatus(String status);

    List<Booking> findAllByStatusAndCreatorId(String status, UUID userid);
}
