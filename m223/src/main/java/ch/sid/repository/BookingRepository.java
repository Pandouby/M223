package ch.sid.repository;

import ch.sid.model.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
    Optional<Booking> findByCreatorId(Long id);

    List<Booking> findAllByStatus(String status);
}
