package ch.sid;

import ch.sid.model.Booking;
import ch.sid.security.JwtServiceHMAC;
import ch.sid.service.BookingService;
import ch.sid.service.MemberService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.*;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtServiceHMAC jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BookingService bookingService;

    private String accessToken;
    private ObjectMapper mapper;

    @BeforeEach
    public void init() {
        accessToken = jwtService.createNewJWT(UUID.randomUUID().toString(), "4be5f5bf-8eb5-44ea-8eb5-a5e807856d09", "peter@rutschmann@gmail.com", List.of("ADMIN"));
         mapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }


    @Test
    @Description("Tests if all entries in the inital db are reachable")
    public void getAllBookings() throws Exception {
        MvcResult response = mockMvc.perform(get("/bookings").header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<Booking> bookings = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertTrue(bookings.size() >= 1);
    }

    @Test
    @Description("Test get booking by user id")
    public void getBookingByUserId() throws Exception {
        MvcResult response = mockMvc.perform(get("/bookings?userid=4be5f5bf-8eb5-44ea-8eb5-a5e807856d09").header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<Booking> bookings = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertTrue(bookings.size() >= 1);
    }

    @Test
    @Description("Get all Bookings with certain status")
    public void getBookingsByStaus() throws Exception {
        MvcResult response = mockMvc.perform(get("/bookings?status?=PENDING").header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<Booking> bookings = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertTrue(bookings.size() >= 1);
    }


    @Test
    @Description("adding a booking with given user")
    public void createValidBooking() throws Exception {
        Booking booking = new Booking();
        booking.setDate(LocalDate.now());
        booking.setDayDuration(1);

        String requestBody = mapper.writeValueAsString(booking);

        MvcResult response = mockMvc.perform(post("/bookings").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        Booking res = mapper.readValue(response.getResponse().getContentAsString(), Booking.class);
        Booking responseBooking = bookingService.getBookingById(res.getId());
        assertTrue(responseBooking != null);
        assertEquals(1, responseBooking.getDayDuration());
        assertEquals("PENDING", responseBooking.getStatus());
        assertEquals(LocalDate.now(), responseBooking.getDate());
    }

    @Test
    @Description("throws exception when date of booking is in the past")
    public void createBookingWithInvalidDate() throws Exception {

        Booking booking = new Booking();
        booking.setDate(LocalDate.now().minusDays(3));
        booking.setDayDuration(0.5f);

        String request = mapper.writeValueAsString(booking);

            MvcResult result = mockMvc.perform(post("/bookings").contentType(MediaType.APPLICATION_JSON)
                            .content(request)
                            .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn();

        assertEquals("Invalid date, date in the past in not possible", result.getResponse().getContentAsString());

    }

    @Test
    @Description("Update exisitng booking with id")
    public void updateBooking() throws Exception {
        Booking booking = new Booking();
        booking.setDate(LocalDate.now().plusDays(2));
        booking.setDayDuration(0.5f);
        booking.setStatus("ACCEPTED");

        String requestBody = mapper.writeValueAsString(booking);

        MvcResult response = mockMvc.perform(put("/bookings/413e2297-b84b-42ef-97ed-16a8a9d1d671").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        Booking res = mapper.readValue(response.getResponse().getContentAsString(), Booking.class);
        Booking responseBooking = bookingService.getBookingById(res.getId());
        assertTrue(responseBooking != null);
        assertEquals(0.5f, booking.getDayDuration());
        assertEquals("ACCEPTED", responseBooking.getStatus());
        assertEquals(LocalDate.now().plusDays(2), responseBooking.getDate());

    }

    /*@Test
    @Description("Test if the admin can update the reservation for the user")
    public void testAdminFunction() throws Exception {

        ControlReservation controlReservation = new ControlReservation();
        controlReservation.setAnswer("denied");
        controlReservation.setId(1L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String req = ow.writeValueAsString(controlReservation);

        val res = mockMvc.perform(put("/api/book/authorize").contentType(MediaType.APPLICATION_JSON)
                        .content(req)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        Reservation reservation = objectMapper.readValue(res.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertEquals(reservation.getAccepted(), 2);
    }

    @Test
    @Description("Testing Status Exception if a wrong one is given")
    public void testStatusException() throws Exception {
        ControlReservation controlReservation = new ControlReservation();
        controlReservation.setAnswer("gibberish");
        controlReservation.setId(1L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String req = ow.writeValueAsString(controlReservation);

        val res = mockMvc.perform(put("/api/book/authorize").contentType(MediaType.APPLICATION_JSON)
                        .content(req)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Description("Testing if getting unknown status for a member throws error")
    public void testGetBookingStatusNotFound() throws Exception {
        val res = mockMvc.perform(get("/api/book/status/67765").header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Description("Testing if delete not found works works")
    public void testCancellingBooking() throws Exception {
        val res = mockMvc.perform(get("/api/book/cancel/75466547").header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Description("Testing deletion function")
    public void testCancellationOfReservation() throws Exception {
        // configure date
        Clock clock = Clock.fixed(Instant.parse("2022-09-20T7:00:00Z"), ZoneId.of("UTC"));
        Clock clockEnd = Clock.fixed(Instant.parse("2022-09-20T12:00:00Z"), ZoneId.of("UTC"));

        LocalDateTime startDateTime = LocalDateTime.now(clock);
        LocalDateTime endDateTime = LocalDateTime.now(clockEnd);

        Reservation reservation = new Reservation();
        reservation.setAccepted(1);
        reservation.setStartDate(startDateTime);
        reservation.setEndDate(endDateTime);
        reservation.setId(3);
        if (placeRepo.findById(1L).isEmpty() || userRepo.findById(1L).isEmpty()) {
            throw new Exception("Data in db couldn't be found");
        }
        reservation.setPlace(placeRepo.findById(2L).get());
        reservation.setUser(userRepo.findById(1L).get());

        reservationRepo.save(reservation);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String req = ow.writeValueAsString(reservation);

        val res = mockMvc.perform(get("/api/book/cancel/3 ").contentType(MediaType.APPLICATION_JSON)
                        .content(req)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }*/
}