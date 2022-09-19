package ch.sid.repository;

import org.springframework.data.repository.CrudRepository;

public interface BookingRepository extends CrudRepository<Long, BookingRepository> {
}
