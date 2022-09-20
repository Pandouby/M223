package ch.sid.controller;

import ch.sid.model.Booking;
import ch.sid.model.User;
import ch.sid.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@Controller
@RequestMapping("booking")
public class BookingController {

    BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity getBookings() {
        return bookingService.getBookings();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getBookingByUser(@PathVariable Long id) {
        return bookingService.getBookingByUser(id);
    }

    @GetMapping("/{status}")
    ResponseEntity getBookingByStatus(@PathVariable String status) {
        return bookingService.getBookingByStatus(status);
    }

    @PostMapping
    public ResponseEntity createBooking(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody Booking booking) {
        return bookingService.update(id, booking);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity updateStatus(@PathVariable Long id, @RequestBody Booking booking){
        return bookingService.updateStatus(id, booking.getStatus());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        return bookingService.delete(id);
    }
}
