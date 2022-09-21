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
import java.util.List;
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

    public List<Booking> getBookings() {
        return (List<Booking>) bookingRepository.findAll();
    }

    public Booking getBookingById(UUID id) {
        if(bookingRepository.existsById(id)){
            return bookingRepository.findById(id).get();
        }else {
            return null;
        }

    }

    public List<Booking> getBookingByUser(UUID id) {
        if(memberRepository.existsById(id)){
            return bookingRepository.findAllByCreatorId(id);
        }else {
            return null;
        }
    }

    public List<Booking> getBookingByStatus(String status) {
        return bookingRepository.findAllByStatus(status);
    }

    public Booking createBooking(Booking booking, String token) throws GeneralSecurityException, IOException {
        token = token.substring(7);
        DecodedJWT decode = jwtService.verifyJwt(token, true);
        String userId = decode.getClaim("user_id").asString();
        Member member = memberRepository.findById(UUID.fromString(userId)).get();
        booking.setStatus("PENDING");
        booking.setCreator(member);

        if(booking.getDate().isBefore(LocalDate.now())) {
            return null;
        }
        System.out.println("booking: " + booking.getId());
        bookingRepository.save(booking);
        return booking;
    }

    public Booking update(UUID id, Booking booking) {
        boolean bookingExists = bookingRepository.existsById(id);
        if(bookingExists){
            Booking bookingToUpdate = bookingRepository.findById(id).get();
            bookingToUpdate.setDayDuration(booking.getDayDuration());
            bookingToUpdate.setDate(booking.getDate());
            bookingToUpdate.setStatus(booking.getStatus());
            bookingRepository.save(bookingToUpdate);
            return bookingToUpdate;
        }else{
            return null;
        }
    }

    public Booking updateStatus (UUID id, Booking booking, String token) throws GeneralSecurityException, IOException {
        boolean bookingExists = bookingRepository.existsById(id);
        token = token.substring(7);
        DecodedJWT decoded = jwtService.verifyJwt(token, true);
        String user_id = decoded.getClaim("user_id").asString();
        String[] scope = decoded.getClaim("scope").asArray(String.class);
        String email = decoded.getClaim("name").asString();
        Member memberSelf = memberRepository.findByEmail(email).get();
        if(!bookingExists){
            return null;
        }else if(memberSelf.getRole().equals("ADMIN")){
            Booking bookingToUpdate = bookingRepository.findById(id).get();
            bookingToUpdate.setStatus(booking.getStatus());
            bookingRepository.save(bookingToUpdate);
            return bookingToUpdate;
        }else if(bookingRepository.findById(id).get().getCreator().getId().equals(UUID.fromString(user_id)) && booking.getStatus().equals("CANCELLED")){
            Booking bookingToUpdate = bookingRepository.findById(id).get();
            bookingToUpdate.setStatus(booking.getStatus());
            bookingRepository.save(bookingToUpdate);
            return bookingToUpdate;
        }else{
            return null;
        }
    }

    public boolean delete (UUID id) {
        if(bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }

    public List<Booking> getBookingsByStatusAndUserId(String status, UUID userid) {
        boolean userExists = memberRepository.existsById(userid);
        if(userExists){
            return bookingRepository.findAllByStatusAndCreatorId(status, userid);
        }else{
            return null;
        }
    }




}
