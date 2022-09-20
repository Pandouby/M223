package ch.sid.service;

import ch.sid.model.Booking;
import ch.sid.model.Member;
import ch.sid.repository.BookingRepository;
import ch.sid.repository.MemberRepository;
import ch.sid.security.JwtServiceHMAC;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class BookingService {

    BookingRepository bookingRepository;
    MemberRepository memberRepository;
    JwtServiceHMAC jwtService;

    @Autowired
    public BookingService(BookingRepository bookingRepository, MemberRepository userRepository, JwtServiceHMAC jwtService) {
        this.bookingRepository = bookingRepository;
        this.memberRepository = userRepository;
        this.jwtService = jwtService;
    }

    public ResponseEntity getBookings() {
        return new ResponseEntity(bookingRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity getBookingByUser(UUID id) {
        if(memberRepository.existsById(id)){
            return new ResponseEntity(bookingRepository.findByCreatorId(id).get(), HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    public ResponseEntity getBookingByStatus(String status) {
        return new ResponseEntity(bookingRepository.findAllByStatus(status), HttpStatus.OK);
    }

    public ResponseEntity createBooking(Booking booking, String token) throws GeneralSecurityException, IOException {
        token = token.substring(7);
        DecodedJWT decode = jwtService.verifyJwt(token, true);
        String userId = decode.getClaim("user_id").asString();
        Member member = memberRepository.findById(UUID.fromString(userId)).get();
        booking.setStatus("PENDING");
        booking.setCreator(member);

        if(booking.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid date, date in the past in not possible");
        }

        bookingRepository.save(booking);
        return new ResponseEntity(booking, HttpStatus.OK);
    }

    public ResponseEntity update(UUID id, Booking booking) {
        if(memberRepository.existsById(id)){
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

    public ResponseEntity updateStatus (UUID id, String status) {
        if(memberRepository.existsById(id)){
            Booking booking = bookingRepository.findById(id).get();
            booking.setStatus(status);
            bookingRepository.save(booking);
            return new ResponseEntity(booking, HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity delete (UUID id) {
        if(bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Booking> getBookingsByStatusAndUserId(String status, UUID userid) {
        boolean userExists = memberRepository.existsById(userid);
        if(userExists){
            return new ResponseEntity(bookingRepository.findAllByStatusAndCreatorId(status, userid), HttpStatus.OK);
        }else{
            return new ResponseEntity("User with given ID does not exist", HttpStatus.NOT_FOUND);
        }
    }


}
