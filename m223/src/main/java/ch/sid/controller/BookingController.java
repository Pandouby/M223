package ch.sid.controller;

import ch.sid.model.Booking;
import ch.sid.security.JwtServiceHMAC;
import ch.sid.service.BookingService;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;

@Controller
@RequestMapping("booking")
public class BookingController {

    BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(
            summary = "Get all Bookings",
            description = "Get a list of all Bookings",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getBookings() {
        return bookingService.getBookings();
    }


    @Operation(
            summary = "Get Booking by Id",
            description = "Get a Booking by Id",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{id}")
    public ResponseEntity getBookingByUser(@PathVariable UUID id) {
        return bookingService.getBookingByUser(id);
    }

    @Operation(
            summary = "Get all Bookings by status",
            description = "Get a list of Bookings by status",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{status}")
    ResponseEntity getBookingByStatus(@PathVariable String status) {
        return bookingService.getBookingByStatus(status);
    }

    @Operation(
            summary = "Create booking",
            description = "Create a new Booking",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @PostMapping
    public ResponseEntity createBooking(@RequestBody Booking booking, @RequestHeader("Authorization") String token) throws GeneralSecurityException, IOException {
        return bookingService.createBooking(booking, token);
    }

    @Operation(
            summary = "Update booking",
            description = "Update an existing Booking",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable UUID id, @RequestBody Booking booking) {
        return bookingService.update(id, booking);
    }

    @Operation(
            summary = "Update status of Booking",
            description = "Update status of Booking by Id",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/status/{id}")
    public ResponseEntity updateStatus(@PathVariable UUID id, @RequestBody Booking booking){
        return bookingService.updateStatus(id, booking.getStatus());
    }

    @Operation(
            summary = "Delete Booking",
            description = "Delete a Booking by Id",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        return bookingService.delete(id);
    }
}
