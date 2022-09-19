package ch.sid.repository;

import ch.sid.model.Booking;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public interface BookingRepository extends CrudRepository<Booking, Long> {
    Optional<Booking> findByCreator(Long id);

    List<Booking> findAllByStatus(String status);
}
