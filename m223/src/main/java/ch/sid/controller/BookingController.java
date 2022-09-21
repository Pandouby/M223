package ch.sid.controller;

import ch.sid.model.Booking;
import ch.sid.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("bookings")
public class BookingController {

    BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(
            summary = "Get Bookings",
            description = "Get all existing Bookings",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Booking>> getAllBookings(@RequestParam(value = "status", required = false) String status, @RequestParam(value = "userid", required = false) UUID userid){
        if(status != null && userid != null){
            return new ResponseEntity(bookingService.getBookingsByStatusAndUserId(status, userid), HttpStatus.OK);
        } else if(status != null){
            return new ResponseEntity<>(bookingService.getBookingByStatus(status), HttpStatus.OK);
        } else if(userid != null){
            return new ResponseEntity<>(bookingService.getBookingByUser(userid), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(bookingService.getBookings(), HttpStatus.OK);
        }
    }

    @Operation(
            summary = "Create booking",
            description = "Create a new Booking",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @PostMapping
    public ResponseEntity createBooking(@RequestBody Booking booking, @RequestHeader("Authorization") String token) throws GeneralSecurityException, IOException {
        Booking returnBooking;
        if((returnBooking = bookingService.createBooking(booking, token)) != null) {
            return new ResponseEntity(returnBooking, HttpStatus.OK);
        }else {
            return  new ResponseEntity("Invalid date, date in the past in not possible", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Update booking",
            description = "Update an existing Booking",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable UUID id, @RequestBody Booking booking) {
        Booking returnBooking;
        if((returnBooking = bookingService.update(id, booking)) != null) {
            return new ResponseEntity(returnBooking, HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Update status of Booking",
            description = "Update status of Booking by Id. NOTE: the status can only be changed to CANCELED",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/status/{id}")
    public ResponseEntity updateStatus(@PathVariable UUID id, @RequestBody Booking booking,  @RequestHeader("Authorization") String token) throws GeneralSecurityException, IOException {
        Booking returnBooking;
        if((returnBooking = bookingService.updateStatus(id, booking, token)) != null) {
            return new ResponseEntity(returnBooking, HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Delete Booking",
            description = "Delete a Booking by Id",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        if(bookingService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return  new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
