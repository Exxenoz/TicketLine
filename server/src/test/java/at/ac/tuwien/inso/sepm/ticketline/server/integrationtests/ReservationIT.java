package at.ac.tuwien.inso.sepm.ticketline.server.integrationtests;

import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.CreateReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationSearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.performance.PerformanceMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.reservation.ReservationMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.seat.SeatMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtests.base.BaseIT;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReservationIT extends BaseIT {

    private static final String RESERVATION_ENDPOINT = "/reservation";
    private static Long PERFORMANCE_TEST_ID = 1L;
    private static Long RESERVATION_TEST_ID = 1L;
    private static Long CUSTOMER_TEST_ID = 1L;

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
    @Autowired
    private PerformanceMapper performanceMapper;
    @Autowired
    private SeatMapper seatMapper;


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
        PERFORMANCE_TEST_ID = performance.getId();

        Seat seat = seatRepository.save(newSeat());
        Customer customer = customerRepository.save(newCustomer());
        CUSTOMER_TEST_ID = customer.getId();

        var seats = new LinkedList<Seat>();
        seats.add(seat);
        var reservation = Reservation.Builder.aReservation()
            .withCustomer(customer)
            .withPerformance(performance)
            .withSeats(seats)
            .withPaid(false)
            .withReservationNumber("ABCD12")
            .build();

        reservation = reservationRepository.save(reservation);
        RESERVATION_TEST_ID = reservation.getId();
    }

    @After
    public void tearDown() {
        reservationRepository.deleteAll();
        performanceRepository.deleteAll();
        artistRepository.deleteAll();
        customerRepository.deleteAll();
        seatRepository.deleteAll();
    }

    @Test
    public void purchaseReservationAsUser() {
        //get not yet purchased Reservation
        var reservation = reservationRepository.findByPaidFalseAndId(RESERVATION_TEST_ID);
        ReservationDTO reservationDTO = reservationMapper.reservationToReservationDTO(reservation);
        Assert.assertNotNull(reservationDTO);
        Assert.assertNull(reservationDTO.getPaidAt());

        //create and send request - purchase reservation
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(reservationDTO)
            .when().post(RESERVATION_ENDPOINT + "/purchase")
            .then().extract().response();

        //assert result
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        var result = response.getBody().as(ReservationDTO.class);
        Assert.assertNotNull(result.getId());
        Assert.assertEquals(true, result.isPaid());
        Assert.assertNotNull(result.getPaidAt());
    }

    @Test
    public void findReservationWithCustomerNameAsUser() {
        //get findAll parameters
        var customerOpt = customerRepository.findById(CUSTOMER_TEST_ID);
        var performanceOpt = performanceRepository.findById(PERFORMANCE_TEST_ID);

        if (customerOpt.isPresent() && performanceOpt.isPresent()) {
            var customerDTO = customerMapper.customerToCustomerDTO(customerOpt.get());
            var performanceDTO = performanceMapper.performanceToPerformanceDTO(performanceOpt.get());
            Assert.assertNotNull(customerDTO);
            Assert.assertNotNull(performanceDTO);

            //create reservation findAll DTO
            var reservationSearchDTO = ReservationSearchDTO.Builder.aReservationSearchDTO()
                .withFirstName(customerDTO.getFirstName())
                .withLastName(customerDTO.getLastName())
                .withPerformanceName(performanceDTO.getName())
                .withPage(0)
                .withSize(10)
                .withSortColumnName("id")
                .withSortDirection(Sort.Direction.ASC)
                .build();

            //create and send request - find not yet purchased reservation from the customer and for the performance
            //with the given name
            RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
                .body(reservationSearchDTO)
                .when().post(RESERVATION_ENDPOINT + "/find")
                .then().statusCode(HttpStatus.OK.value())
                .assertThat().body("content.id", Matchers.hasItem(PERFORMANCE_TEST_ID.intValue()))
                .assertThat().body("content.paid", Matchers.hasItem(false));
        } else {
            Assert.fail("Either the Customer or the Performance were not found!");
        }
    }

    @Test
    public void removeSeatFromReservationAsUser() {
        //get not yet purchased reservation
        var reservation = reservationRepository.findByPaidFalseAndId(RESERVATION_TEST_ID);
        ReservationDTO reservationDTO = reservationMapper.reservationToReservationDTO(reservation);
        Assert.assertNotNull(reservationDTO);

        //edit reservation -> remove one seat
        var seatDTOs = reservationDTO.getSeats();
        Assert.assertEquals(1, seatDTOs.size());
        seatDTOs.remove(0);
        reservationDTO.setSeats(seatDTOs);

        //create and send request - update reservation with new data
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(reservationDTO)
            .when().post(RESERVATION_ENDPOINT + "/edit")
            .then().extract().response();

        //assert result
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        var result = response.getBody().as(ReservationDTO.class);
        Assert.assertNotNull(result.getId());
        var resultSeatDTOs = result.getSeats();
        Assert.assertEquals(0, resultSeatDTOs.size());
    }

    @Test
    public void addLockedSeatToReservationAsUser() {
        //get not yet purchased reservation
        var reservation = reservationRepository.findByPaidFalseAndId(RESERVATION_TEST_ID);
        ReservationDTO reservationDTO = reservationMapper.reservationToReservationDTO(reservation);
        Assert.assertNotNull(reservationDTO);

        //edit reservation -> remove one seat
        var seatDTOs = reservationDTO.getSeats();
        var lockedSeat = seatRepository.save(newSeat());
        Assert.assertEquals(1, seatDTOs.size());
        seatDTOs.add(SeatDTO.Builder.aSeatDTO()
            .withPositionX(lockedSeat.getPositionX())
            .withPositionY(lockedSeat.getPositionY())
            .build()
        );
        reservationDTO.setSeats(seatDTOs);

        //create and send request - update reservation with new data
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(reservationDTO)
            .when().post(RESERVATION_ENDPOINT + "/edit")
            .then().extract().response();

        //assert result
        Assert.assertEquals(HttpStatus.CONFLICT.value(), response.getStatusCode());
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
                .withSeats(List.of(seatMapper.seatToSeatDTO(seat)))
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
                .withSeats(List.of(seatMapper.seatToSeatDTO(seat)))
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
        performance.setPrice(100L);
        performance.setPerformanceStart(LocalDateTime.now());
        performance.setDuration(Duration.ofMinutes(30));

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
        customer.setFirstName("firstname");
        customer.setLastName("lastname");
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
