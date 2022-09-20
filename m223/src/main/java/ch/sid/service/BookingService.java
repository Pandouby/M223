package ch.sid.service;

import ch.sid.model.Booking;
import ch.sid.repository.BookingRepository;
import ch.sid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    BookingRepository bookingRepository;
    UserRepository userRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity getBookings() {
        return new ResponseEntity(bookingRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity getBookingByUser(Long id) {
        if(userRepository.existsById(id)){
            return new ResponseEntity(bookingRepository.findByCreatorId(id).get(), HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    public ResponseEntity getBookingByStatus(String status) {
        return new ResponseEntity(bookingRepository.findAllByStatus(status), HttpStatus.OK);
    }

    public ResponseEntity createBooking(Booking booking) {
        bookingRepository.save(booking);
        return new ResponseEntity(booking, HttpStatus.OK);
    }

    public ResponseEntity update(Long id, Booking booking) {
        if(userRepository.existsById(id)){
            Booking tempBooking = bookingRepository.findById(id).get();
            tempBooking.setCreator(booking.getCreator());
            tempBooking.setDate(booking.getDate());
            tempBooking.setDayDuration(booking.getDayDuration());
            tempBooking.setStatus(booking.getStatus());
            return new ResponseEntity(tempBooking, HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity updateStatus (Long id, String status) {
        if(userRepository.existsById(id)){
            Booking booking = bookingRepository.findById(id).get();
            booking.setStatus(status);
            bookingRepository.save(booking);
            return new ResponseEntity(booking, HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity delete (Long id) {
        if(bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }



}
