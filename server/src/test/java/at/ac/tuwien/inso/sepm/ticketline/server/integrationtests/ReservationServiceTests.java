package at.ac.tuwien.inso.sepm.ticketline.server.integrationtests;


import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalCancelationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalSeatReservationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InvalidReservationException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.*;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("unit-test")
public class ReservationServiceTests {
    @Autowired
    private ReservationService reservationService;

    private static Long RESERVATION_TEST_ID = 1L;
    private static Long CUSTOMER_TEST_ID = 1L;
    private static Long SEAT_TEST_ID = 1L;
    private static Long PERFORMANCE_TEST_ID = 1L;
    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private CustomerRepository customerRepository;
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
        seatRepository.deleteAll();
        sectorRepository.deleteAll();
        hallRepository.deleteAll();
        sectorCategoryRepository.deleteAll();
    }


    @After
    public void tearDown() {
    }


    @Test
    public void removeSeatFromReservation() throws InvalidReservationException {
        //get reservation
        Performance performance = newPerformance();
        Seat seat = seatRepository.save(newSeat());
        performance.setHall(hallforPerformances);
        performance = performanceRepository.save(performance);

        List<Seat> seats = new LinkedList<>();
        seats.add(seat);
        Customer customer = customerRepository.save(newCustomer());

        Reservation reservation = new Reservation();
        reservation.setPaid(false);
        reservation.setSeats(seats);
        reservation.setPerformance(performance);
        reservation.setCustomer(customer);
        reservation.setReservationNumber("000000");

        reservation = reservationRepository.save(reservation);
        Assert.assertNotNull(reservation);
        Assert.assertEquals(false, reservation.isPaid());

        //remove seats
        seats = reservation.getSeats();
        Assert.assertEquals(1, seats.size());
        seat = seats.get(0);
        seats.remove(seat);
        reservation.setSeats(seats);
        reservation = reservationService.editReservation(reservation);

        //assert result
        Assert.assertNotNull(reservation);
        seats = reservation.getSeats();
        Assert.assertEquals(0, seats.size());
        var seatOpt = seatRepository.findById(SEAT_TEST_ID);
        Assert.assertFalse(seatOpt.isPresent());
    }

    @Test(expected = InvalidReservationException.class)
    public void addAlreadyReservedSeatToReservationShouldThrowInvalidReservationException() throws InvalidReservationException {
        Performance performance = newPerformance();
        Seat seat = seatRepository.save(newSeat());
        performance.setHall(hallforPerformances);
        performance = performanceRepository.save(performance);

        LinkedList<Seat> seats = new LinkedList<>();
        seats.add(seat);
        Customer customer = customerRepository.save(newCustomer());

        Reservation reservation = new Reservation();
        reservation.setPaid(false);
        reservation.setSeats(seats);
        reservation.setPerformance(performance);
        reservation.setCustomer(customer);
        reservation.setReservationNumber("000000");

        reservation = reservationRepository.save(reservation);

        //create reservation
        Seat newSeat = Seat.SeatBuilder.aSeat()
            .withPositionX(5)
            .withPositionY(5)
            .withSector(newSector())
            .build();
        newSeat = seatRepository.save(newSeat);
        performance.setHall(hallforPerformances);
        performanceRepository.save(performance);

        Reservation newReservation = Reservation.Builder.aReservation()
            .withPaid(false)
            .withCustomer(customer)
            .withPerformance(performance)
            .withSeats(List.of(seat))
            .withReservationNumber("1111111")
            .build();
        newReservation = reservationRepository.save(reservation);

        //get reservation
        Assert.assertNotNull(reservation);
        Assert.assertEquals(false, reservation.isPaid());

        //try to reserve already reserved Seat
        List<Seat> oldSeats = reservation.getSeats();
        Assert.assertEquals(1, oldSeats.size());
        Seat invalidSeat = Seat.SeatBuilder.aSeat()
            .withPositionX(5)
            .withPositionY(5)
            .withSector(oldSeats.get(0).getSector())
            .build();
        seats.add(invalidSeat);
        reservation.setSeats(seats);
        reservationService.editReservation(reservation);//<-- should throw the exception
    }


    @Test
    public void purchaseReservation() {
        //get reservation
        Performance performance = newPerformance();
        Seat seat = seatRepository.save(newSeat());
        performance.setHall(hallforPerformances);
        performance = performanceRepository.save(performance);

        LinkedList<Seat> seats = new LinkedList<>();
        seats.add(seat);
        Customer customer = customerRepository.save(newCustomer());

        Reservation reservation = new Reservation();
        reservation.setPaid(false);
        reservation.setSeats(seats);
        reservation.setPerformance(performance);
        reservation.setCustomer(customer);
        reservation.setReservationNumber("000000");

        reservation = reservationRepository.save(reservation);

        Assert.assertNotNull(reservation);
        Assert.assertEquals(false, reservation.isPaid());
        reservationService.purchaseReservation(reservation);
        //assert result
        Assert.assertEquals(true, reservation.isPaid());
        Assert.assertNull(reservationService.findOneByPaidFalseAndId(reservation.getId()));
    }

    @Test
    public void purchaseReservationWithCostumerAndPerformance() {
        Performance performance = newPerformance();
        Seat seat = seatRepository.save(newSeat());
        performance.setHall(hallforPerformances);
        performance = performanceRepository.save(performance);

        LinkedList<Seat> seats = new LinkedList<>();
        seats.add(seat);
        Customer customer = customerRepository.save(newCustomer());

        Reservation createdReservation = new Reservation();
        createdReservation.setPaid(false);
        createdReservation.setSeats(seats);
        createdReservation.setPerformance(performance);
        createdReservation.setCustomer(customer);
        createdReservation.setReservationNumber("000000");

        createdReservation = reservationRepository.save(createdReservation);
        //Search Parameters
        var reservationSearch = ReservationSearch.Builder.aReservationSearch()
            .withFirstName(customer.getFirstName())
            .withLastName(customer.getLastName())
            .withPerfomanceName(performance.getName())
            .build();


        //search
        Pageable pageable = Pageable.unpaged();
        var reservationPage = reservationService.findAllByCustomerNameAndPerformanceName(
            reservationSearch, pageable);
        var reservations = reservationPage.getContent();
        Assert.assertEquals(1, reservations.size());
        //purchase said reservations
        for (Reservation item : reservations) {
            reservationService.purchaseReservation(item);
        }

        //assert result
        reservationPage = reservationService.findAllByCustomerNameAndPerformanceName(
            reservationSearch, pageable);
        reservations = reservationPage.getContent();
        Assert.assertEquals(1, reservations.size());
        Assert.assertTrue(reservations.get(0).isPaid());
    }

    @Test
    public void findReservationWithCustomerAndPerformance() {
        Performance performance = newPerformance();
        Seat seat = seatRepository.save(newSeat());
        performance.setHall(hallforPerformances);
        performance = performanceRepository.save(performance);

        LinkedList<Seat> seats = new LinkedList<>();
        seats.add(seat);
        Customer customer = customerRepository.save(newCustomer());

        Reservation createdReservation = new Reservation();
        createdReservation.setPaid(false);
        createdReservation.setSeats(seats);
        createdReservation.setPerformance(performance);
        createdReservation.setCustomer(customer);
        createdReservation.setReservationNumber("000000");

        createdReservation = reservationRepository.save(createdReservation);

        var reservationSearch = ReservationSearch.Builder.aReservationSearch()
            .withFirstName(customer.getFirstName())
            .withLastName(customer.getLastName())
            .withPerfomanceName(performance.getName())
            .build();


        //search
        Pageable pageable = Pageable.unpaged();
        var reservationPage = reservationService.findAllByCustomerNameAndPerformanceName(
            reservationSearch, pageable);
        var reservations = reservationPage.getContent();

        //assert result
        Assert.assertEquals(1, reservations.size());
        var reservation = reservations.get(0);
        var actualCustomer = reservation.getCustomer();
        var actualReservationId = reservation.getId();
        Assert.assertEquals(customer, actualCustomer);
        Assert.assertEquals(createdReservation.getId(), actualReservationId);
    }

    @Test
    public void searchForReservationWithWrongCustomerAndPerformanceDataShouldGetEmptyResult() {
        Performance performance = newPerformance();
        Seat seat = seatRepository.save(newSeat());
        performance.setHall(hallforPerformances);
        performance = performanceRepository.save(performance);

        LinkedList<Seat> seats = new LinkedList<>();
        seats.add(seat);
        Customer customer = customerRepository.save(newCustomer());

        Reservation createdReservation = new Reservation();
        createdReservation.setPaid(false);
        createdReservation.setSeats(seats);
        createdReservation.setPerformance(performance);
        createdReservation.setCustomer(customer);
        createdReservation.setReservationNumber("000000");

        createdReservation = reservationRepository.save(createdReservation);

        var reservationSearch = ReservationSearch.Builder.aReservationSearch()
            .withFirstName("bla")
            .withLastName("bla")
            .withPerfomanceName("bla")
            .build();


        //search
        Pageable pageable = Pageable.unpaged();
        var reservationPage = reservationService.findAllByCustomerNameAndPerformanceName(
            reservationSearch, pageable);
        var reservations = reservationPage.getContent();

        //assert result
        Assert.assertEquals(0, reservations.size());
    }

    @Test
    public void createReservation() {
        Performance performance = performanceRepository.save(newPerformance());
        Seat seat = newSeat();
        Customer customer = customerRepository.save(newCustomer());
        performance.setHall(hallforPerformances);
        performanceRepository.save(performance);

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setPerformance(performance);
        reservation.setSeats(List.of(seat));
        // reservation.setReservationNumber("000001");

        Reservation returned = null;
        try {
            returned = reservationService.createReservation(reservation);
        } catch (InvalidReservationException e) {
            fail();
        } catch (InternalSeatReservationException e) {
            e.printStackTrace();
        }

        assertThat(reservation.getId(), is(returned.getId()));
        assertThat(reservation.getCustomer(), is(returned.getCustomer()));
        assertThat(reservation.getSeats(), is(returned.getSeats()));
        assertThat(reservation.getPerformance(), is(returned.getPerformance()));
        assertThat(reservation.getReservationNumber(), is(returned.getReservationNumber()));
        assertThat(reservation.getPaid(), is(false));

    }


    @Test(expected = InternalSeatReservationException.class)
    public void createInvalidReservation() throws InvalidReservationException, InternalSeatReservationException {
        Performance performance = performanceRepository.save(newPerformance());
        Seat seat = newSeat();
        Customer customer = customerRepository.save(newCustomer());
        performance.setHall(hallforPerformances);
        performanceRepository.save(performance);

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setPerformance(performance);
        reservation.setSeats(List.of(seat));
        reservation.setReservationNumber("000003");

        Reservation reservation2 = new Reservation();
        reservation2.setCustomer(customer);
        reservation2.setPerformance(performance);
        reservation2.setSeats(List.of(seat));
        reservation2.setReservationNumber("000004");

        reservationService.createReservation(reservation);
        reservationService.createReservation(reservation2);


    }

    @Test
    public void createAndPay() {
        Performance performance = performanceRepository.save(newPerformance());
        Seat seat = newSeat();
        Customer customer = customerRepository.save(newCustomer());
        performance.setHall(hallforPerformances);
        performanceRepository.save(performance);

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setPerformance(performance);
        reservation.setSeats(List.of(seat));

        Reservation returned = null;
        try {
            returned = reservationService.createAndPayReservation(reservation);
        } catch (InvalidReservationException e) {
            fail();
        } catch (InternalSeatReservationException e) {
            fail();
        }

        assertThat(reservation.getId(), is(returned.getId()));
        assertThat(reservation.getCustomer(), is(returned.getCustomer()));
        assertThat(reservation.getSeats(), is(returned.getSeats()));
        assertThat(reservation.getPerformance(), is(returned.getPerformance()));
        assertThat(reservation.getPaid(), is(true));

    }

    @Test
    public void cancel() throws InternalCancelationException {
        Performance performance = performanceRepository.save(newPerformance());
        Seat seat = newSeat();
        Customer customer = customerRepository.save(newCustomer());
        performance.setHall(hallforPerformances);
        performanceRepository.save(performance);

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setPerformance(performance);
        reservation.setSeats(List.of(seat));
        reservation.setReservationNumber("000005");

        Reservation returned = null;
        try {
            reservation = reservationService.createReservation(reservation);
            returned = reservationService.cancelReservation(reservation.getId());
        } catch (InvalidReservationException e) {
            fail();
        } catch (InternalSeatReservationException e) {
            fail();
        }

        assertThat(returned.isCanceled(), is(true));
    }

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
