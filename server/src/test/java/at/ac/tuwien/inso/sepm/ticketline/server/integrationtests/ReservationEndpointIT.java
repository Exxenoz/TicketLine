package at.ac.tuwien.inso.sepm.ticketline.server.integrationtests;

import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.CreateReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationSearchDTO;
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
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReservationEndpointIT extends BaseIT {

    private static final String RESERVATION_ENDPOINT = "/reservation";
    private static final String RESERVATIONNUMBER_TEST = "0000000";
    private static final String RESERVATIONNUMBER_TEST2 = "0000001";

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
    @Autowired
    private SectorCategoryRepository sectorCategoryRepository;
    @Autowired
    private SectorRepository sectorRepository;
    @Autowired
    private HallRepository hallRepository;

    private Hall hallforPerformances;

    @Before
    public void setUp() {
        reservationRepository.deleteAll();
        performanceRepository.deleteAll();
        customerRepository.deleteAll();
        artistRepository.deleteAll();
        seatRepository.deleteAll();
        hallRepository.deleteAll();
        sectorRepository.deleteAll();
        sectorCategoryRepository.deleteAll();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void purchaseReservationAsUser() {
        Performance performance = newPerformance();
        Seat seat = newSeat();
        Customer customer = customerRepository.save(newCustomer());
        performance.setHall(hallforPerformances);
        performance = performanceRepository.save(performance);
        Reservation reservation = Reservation.Builder.aReservation()
            .withCustomer(customer)
            .withPaid(false)
            .withPerformance(performance)
            .withSeats(List.of(seat))
            .withReservationNumber(RESERVATIONNUMBER_TEST)
            .build();
        reservation = reservationRepository.save(reservation);
        var reservationDTO = reservationMapper.reservationToReservationDTO(reservation);

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
        Performance performance = newPerformance();
        Seat seat = newSeat();
        Customer customer = customerRepository.save(newCustomer());
        performance.setHall(hallforPerformances);
        performance = performanceRepository.save(performance);
        Reservation reservation = Reservation.Builder.aReservation()
            .withCustomer(customer)
            .withPaid(false)
            .withPerformance(performance)
            .withSeats(List.of(seat))
            .withReservationNumber(RESERVATIONNUMBER_TEST)
            .build();
        reservation = reservationRepository.save(reservation);

        var reservationDTO = reservationMapper.reservationToReservationDTO(reservation);
        var customerDTO = customerMapper.customerToCustomerDTO(customer);
        var performanceDTO = performanceMapper.performanceToPerformanceDTO(performance);
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
            .assertThat().body("content.id", Matchers.hasItem(reservation.getId().intValue()))
            .assertThat().body("content.paid", Matchers.hasItem(false));
    }

    @Test
    public void removeSeatFromReservationAsUser() {
        Performance performance = newPerformance();
        Seat seat = newSeat();
        Customer customer = customerRepository.save(newCustomer());
        performance.setHall(hallforPerformances);
        performance = performanceRepository.save(performance);
        Reservation reservation = Reservation.Builder.aReservation()
            .withCustomer(customer)
            .withPaid(false)
            .withPerformance(performance)
            .withSeats(List.of(seat))
            .withReservationNumber(RESERVATIONNUMBER_TEST)
            .build();
        reservation = reservationRepository.save(reservation);
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
    public void addAlreadyReservedSeatToReservationAsUserShouldThrowConflictHttpErrorCode() {
        Performance performance = newPerformance();
        Seat seat = newSeat();
        Customer customer = customerRepository.save(newCustomer());
        performance.setHall(hallforPerformances);
        performance = performanceRepository.save(performance);
        Reservation reservation = Reservation.Builder.aReservation()
            .withCustomer(customer)
            .withPaid(false)
            .withPerformance(performance)
            .withSeats(List.of(seat))
            .withReservationNumber(RESERVATIONNUMBER_TEST)
            .build();
        reservation = reservationRepository.save(reservation);


        //new reservation with not reserved seat
        Seat newSeat = Seat.SeatBuilder.aSeat()
            .withPositionX(5)
            .withPositionY(5)
            .withSector(seat.getSector())
            .build();
        newSeat = seatRepository.save(newSeat);
        List<Seat> seats = new LinkedList<>();
        seats.add(newSeat);
        Reservation newReservation = Reservation.Builder.aReservation()
            .withCustomer(customer)
            .withPaid(false)
            .withPerformance(performance)
            .withSeats(seats)
            .withReservationNumber(RESERVATIONNUMBER_TEST2)
            .build();
        newReservation = reservationRepository.save(newReservation);

        //edit reservation so that the the added seat is already reserved
        seats = newReservation.getSeats();
        seats.add(Seat.SeatBuilder.aSeat()
            .withPositionX(seat.getPositionX())
            .withPositionY(seat.getPositionY())
            .withSector(seat.getSector())
            .build());
        newReservation.setSeats(seats);

        ReservationDTO reservationDTO = reservationMapper.reservationToReservationDTO(newReservation);
        Assert.assertNotNull(reservationDTO);

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
        Performance performance = newPerformance();
        Seat seat = newSeat();
        Customer customer = customerRepository.save(newCustomer());
        performance.setHall(hallforPerformances);
        performance = performanceRepository.save(performance);

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
        Performance performance = newPerformance();
        Seat seat = newSeat();
        Customer customer = customerRepository.save(newCustomer());
        performance.setHall(hallforPerformances);
        performance = performanceRepository.save(performance);

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

    //TODO: test for cancel

    private Performance newPerformance() {
        Performance performance = new Performance();
        performance.setName("test");
        performance.setPrice(100L);
        performance.setPerformanceStart(LocalDateTime.now());
        performance.setDuration(Duration.ofMinutes(20));

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
        seat.setSector(newSector());
        return seatRepository.save(seat);
    }

    private Customer newCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("first name");
        customer.setLastName("last name");
        customer.setEmail("email@mail.com");
        customer.setTelephoneNumber("0123456789");
        //customer.setId(CUSTOMER_TEST_ID);


        BaseAddress address = new BaseAddress();
        address.setCity("city");
        address.setCountry("country");
        address.setStreet("street");
        address.setPostalCode("postalCode");
        customer.setBaseAddress(address);

        return customer;
    }

    private Sector newSector() {
        final var sector = new Sector();
        sector.setCategory(newSectorCategory());
        sector.setSeatsPerRow(10);
        sector.setRows(3);
        sector.setStartPositionY(0);
        Sector sector1 = sectorRepository.save(sector);
        Hall hall = newHall();
        hall.setSectors(List.of(sector1));
        hall.setAddress(newLocationAddress());
        hallforPerformances = hallRepository.save(hall);
        return sector1;

    }

    private SectorCategory newSectorCategory() {
        final var sectorCategory = new SectorCategory();
        sectorCategory.setName("test");
        sectorCategory.setBasePriceMod(100L);
        return sectorCategoryRepository.save(sectorCategory);
    }

    private Hall newHall() {
        final var hall = new Hall();
        hall.setName("hall1");
        return hall;
    }

    private LocationAddress newLocationAddress() {
        final var locationAddress = new LocationAddress();
        locationAddress.setLocationName("test");
        locationAddress.setCity("test");
        locationAddress.setPostalCode("1111");
        locationAddress.setCountry("tt");
        locationAddress.setStreet("test");
        return locationAddress;
    }
}




