package at.ac.tuwien.inso.sepm.ticketline.server.integrationtests;

import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.CreateReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.reservation.ReservationMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtests.base.BaseIT;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReservationIT extends BaseIT {

    private static final String RESERVATION_ENDPOINT = "/reservation";
    private static Long RESERVATION_TEST_ID = 1L;

    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ArtistRepository artistRepository;


    @Before
    public void setUp() {
        Artist artist = Artist.Builder.anArtist()
            .withFirstName("artFirstName")
            .withLastName("artLastName")
            .build();
        artist = artistRepository.save(artist);
        var artists = new HashSet<Artist>();
        artists.add(artist);
        Performance performance = newPerformance();
        performance.setArtists(artists);
        performance = performanceRepository.save(performance);
        Seat seat = seatRepository.save(newSeat());
        Customer customer = customerRepository.save(newCustomer());

        var seats = new LinkedList<Seat>();
        seats.add(seat);
        var reservation = Reservation.Builder.aReservation()
            .withCustomer(customer)
            .withPerformance(performance)
            .withSeats(seats)
            .withPaid(false)
            .build();

        reservation = reservationRepository.save(reservation);
        RESERVATION_TEST_ID = reservation.getId();
    }

    @Test
    public void purchaseReservationAsUser() {

        var reservation = reservationRepository.findByPaidFalseAndId(RESERVATION_TEST_ID);

        ReservationDTO reservationDTO = reservationMapper.reservationToReservationDTO(reservation);
        Assert.assertNotNull(reservationDTO);
        Assert.assertNull(reservationDTO.getPaidAt());

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(reservationDTO)
            .when().post(RESERVATION_ENDPOINT + "/purchase")
            .then().extract().response();
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        var result = response.getBody().as(ReservationDTO.class);
        Assert.assertNotNull(result.getId());
        Assert.assertEquals(true, result.isPaid());
        Assert.assertNotNull(result.getPaidAt());
    }

    @Test
    @Transactional
    public void removeSeatFromReservation() {

        var reservation = reservationRepository.findByPaidFalseAndId(RESERVATION_TEST_ID);

        ReservationDTO reservationDTO = reservationMapper.reservationToReservationDTO(reservation);
        Assert.assertNotNull(reservationDTO);
        var seats = reservation.getSeats();
        Assert.assertEquals(1, seats.size());

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(reservationDTO)
            .when().post(RESERVATION_ENDPOINT + "/edit")
            .then().extract().response();
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        var result = response.getBody().as(ReservationDTO.class);
        Assert.assertNotNull(result.getId());

        seats = reservation.getSeats();
        Assert.assertEquals(0, seats.size());
    }

    @Test
    public void createReservationAsUser() {
        // GIVEN
        Performance performance = performanceRepository.save(newPerformance());
        Seat seat = seatRepository.save(newSeat());
        Customer customer = customerRepository.save(newCustomer());

        // WHEN
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(CreateReservationDTO.CreateReservationDTOBuilder.aCreateReservationDTO()
                .withCustomerID(customer.getId())
                .withPaid(false)
                .withPerformanceID(performance.getId())
                .withSeatIDs(List.of(seat.getId()))
                .build())
            .when().post(RESERVATION_ENDPOINT)
            .then().extract().response();
        // THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        ReservationDTO reservationDTO = response.as(ReservationDTO.class);
        assertThat(reservationDTO.getPerformance().getId(), is(performance.getId()));
        assertThat(reservationDTO.getPerformance().getPrice(), is(performance.getPrice()));
        assertThat(reservationDTO.getSeats().get(0).getId(), is(seat.getId()));
        assertThat(reservationDTO.getSeats().get(0).getPositionX(), is(seat.getPositionX()));
        assertThat(reservationDTO.getSeats().get(0).getPositionY(), is(seat.getPositionY()));
    }

    @Test
    public void createAndPayReservationAsUser() {
        // GIVEN
        Performance performance = performanceRepository.save(newPerformance());
        Seat seat = seatRepository.save(newSeat());
        Customer customer = customerRepository.save(newCustomer());

        // WHEN
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(CreateReservationDTO.CreateReservationDTOBuilder.aCreateReservationDTO()
                .withCustomerID(customer.getId())
                .withPaid(false)
                .withPerformanceID(performance.getId())
                .withSeatIDs(List.of(seat.getId()))
                .build())
            .when().post(RESERVATION_ENDPOINT + "/createAndPay")
            .then().extract().response();
        // THEN

        assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        ReservationDTO reservationDTO = response.as(ReservationDTO.class);
        assertThat(reservationDTO.getPerformance().getId(), is(performance.getId()));
        assertThat(reservationDTO.getPerformance().getPrice(), is(performance.getPrice()));
        assertThat(reservationDTO.getSeats().get(0).getId(), is(seat.getId()));
        assertThat(reservationDTO.getSeats().get(0).getPositionX(), is(seat.getPositionX()));
        assertThat(reservationDTO.getSeats().get(0).getPositionY(), is(seat.getPositionY()));
        assertThat(reservationDTO.isPaid(), is(true));
    }

    private Performance newPerformance() {
        Performance performance = new Performance();
        performance.setName("test");
        performance.setPrice(new BigDecimal("1.00"));
        performance.setPerformanceStart(LocalDateTime.now());
        performance.setPerformanceEnd(LocalDateTime.now());

        LocationAddress address = new LocationAddress();
        address.setCity("city");
        address.setCountry("country");
        address.setLocationName("locationName");
        address.setStreet("street");
        address.setPostalCode("postalCode");
        performance.setLocationAddress(address);

        return performance;
    }

    private Seat newSeat() {
        Seat seat = new Seat();
        seat.setPositionX(1);
        seat.setPositionY(2);
        return seat;
    }

    private Customer newCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("first name");
        customer.setLastName("last name");
        customer.setEmail("email@mail.com");
        customer.setTelephoneNumber("0123456789");


        BaseAddress address = new BaseAddress();
        address.setCity("city");
        address.setCountry("country");
        address.setStreet("street");
        address.setPostalCode("postalCode");
        customer.setBaseAddress(address);

        return customer;
    }



}
