package at.ac.tuwien.inso.sepm.ticketline.server.unittests.reservation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalCancelationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalReservationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalSeatReservationException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ReservationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.SeatsService;
import io.jsonwebtoken.lang.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("unit-test")
public class ReservationServiceUnitTests {

    @Autowired
    private ReservationService reservationService;

    private ReservationRepository reservationRepositoryMock;
    private PerformanceRepository performanceRepositoryMock;
    private SeatsService seatsServiceMock;

    private Sector testSector;
    private static final Seat SEAT_TEST = Seat.SeatBuilder.aSeat()
        .withPositionX(2)
        .withPositionY(2)
        .build();
    private static final Long RESERVATION_TEST_ID_1 = 1L;
    private static final String RESERVATION_NUMBER_TEST_1 = "0000000";
    private static final String RESERVATION_NUMBER_TEST_2 = "0000001";
    private static final Long RESERVATION_TEST_ID_2 = 2L;
    private static final Long PERFORMANCE_TEST_ID = 1L;
    private static final Long SEAT_TEST_ID_1 = 1L;
    private static final Long SEAT_TEST_ID_2 = 2L;


    @Before
    public void setUp() {

        reservationRepositoryMock = mock(ReservationRepository.class);
        reservationService.setReservationRepository(reservationRepositoryMock);

        performanceRepositoryMock = mock(PerformanceRepository.class);
        reservationService.setPerformanceRepository(performanceRepositoryMock);

        seatsServiceMock = mock(SeatsService.class);
        reservationService.setSeatsService(seatsServiceMock);

    }


    @Test
    public void createReservation() throws InternalReservationException, InternalSeatReservationException {
        Performance performance = newPerformance();
        Seat seat = newSeat();
        Customer customer = newCustomer();

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setPerformance(performance);
        reservation.setSeats(List.of(seat));
        //  reservation.setReservationNumber("000001");

        when(reservationRepositoryMock.save(reservation)).thenReturn(newReservation(customer, performance, List.of(seat)));
        when(performanceRepositoryMock.findById(performance.getId())).thenReturn(Optional.of(performance));
        when(seatsServiceMock.createSeats(List.of(seat))).thenReturn(List.of(newSeat()));

        Reservation returned = reservationService.createReservation(reservation);

        assertThat(reservation.getId(), is(returned.getId()));
        assertThat(reservation.getCustomer(), is(returned.getCustomer()));
        assertThat(reservation.getSeats(), is(returned.getSeats()));
        assertThat(reservation.getPerformance(), is(returned.getPerformance()));
        Assert.notNull(reservation.getReservationNumber());
        assertThat(reservation.getPaid(), is(false));

    }

    @Test
    public void cancel() throws InternalCancelationException, InternalSeatReservationException, InternalReservationException {
        Performance performance = newPerformance();
        Seat seat = newSeat();
        Customer customer = newCustomer();

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setPerformance(performance);
        reservation.setSeats(List.of(seat));

        when(reservationRepositoryMock.save(reservation)).thenReturn(newReservation(customer, performance, List.of(seat)));
        when(reservationRepositoryMock.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(performanceRepositoryMock.findById(performance.getId())).thenReturn(Optional.of(newPerformance()));
        when(seatsServiceMock.createSeats(List.of(seat))).thenReturn(List.of(newSeat()));

        reservation = reservationService.createReservation(reservation);
        Reservation returned = reservationService.cancelReservation(reservation.getId());

        assertThat(returned.isCanceled(), is(true));
    }

    @Test
    public void purchaseReservation() {
        Performance performance = newPerformance();
        Seat seat = newSeat();
        List<Seat> seats = new LinkedList<>();
        seats.add(seat);
        Customer customer = newCustomer();

        Reservation reservation = newReservation(customer, performance, seats);
        reservation.setId(RESERVATION_TEST_ID_1);
        reservation.setReservationNumber(RESERVATION_NUMBER_TEST_1);

        when(reservationRepositoryMock.save(reservation)).thenReturn(
            Reservation.Builder.aReservation()
                .withCustomer(customer)
                .withPerformance(performance)
                .withReservationNumber(RESERVATION_NUMBER_TEST_1)
                .withSeats(seats)
                .withId(RESERVATION_TEST_ID_1)
                .withPaid(true)
                .withPaidAt(LocalDateTime.now())
                .build()
        );

        Reservation purchasedReservation = reservationService.purchaseReservation(reservation);
        assertTrue(purchasedReservation.isPaid());
        assertNotNull(purchasedReservation.getPaidAt());
    }

    @Test
    public void addSeatToExistingReservation() throws InternalReservationException {
        Performance performance = newPerformance();
        performance.setId(PERFORMANCE_TEST_ID);
        Seat seat = newSeat();
        List<Seat> seats = new LinkedList<>();
        seats.add(seat);
        Customer customer = newCustomer();

        Reservation reservation = newReservation(customer, performance, seats);
        reservation.setId(RESERVATION_TEST_ID_1);
        reservation.setReservationNumber(RESERVATION_NUMBER_TEST_1);
        reservation.setPaid(false);

        Seat newSeat = SEAT_TEST;
        newSeat.setSector(testSector);
        seats.add(newSeat);

        when(reservationRepositoryMock.findByPaidFalseAndId(RESERVATION_TEST_ID_1))
            .thenReturn(
                Reservation.Builder.aReservation()
                    .withId(RESERVATION_TEST_ID_1)
                    .withSeats(List.of(seat))
                    .withReservationNumber(reservation.getReservationNumber())
                    .withPerformance(performance)
                    .withCustomer(customer)
                    .withPaid(false)
                    .withPaidAt(null)
                    .build()
            );
        when(performanceRepositoryMock.findById(PERFORMANCE_TEST_ID)).thenReturn(Optional.of(performance));
        when(seatsServiceMock.createSeats(seats)).thenReturn(seats);
        when(reservationRepositoryMock.save(reservation)).thenReturn(
            Reservation.Builder.aReservation()
                .withId(RESERVATION_TEST_ID_1)
                .withSeats(seats)
                .withReservationNumber(reservation.getReservationNumber())
                .withPerformance(performance)
                .withCustomer(customer)
                .withPaid(false)
                .withPaidAt(null)
                .build()
        );

        reservation.setSeats(seats);
        var updatedReservation = reservationService.editReservation(reservation);
        assertEquals(seats.size(), updatedReservation.getSeats().size());
        for (int i = 0; i < seats.size(); i++) {
            assertEquals(seats.get(i), updatedReservation.getSeats().get(i));
        }
    }

    @Test
    public void removeSeatFromExistingReservation() throws InternalReservationException {
        Performance performance = newPerformance();
        performance.setId(PERFORMANCE_TEST_ID);
        Seat seat = newSeat();
        List<Seat> seats = new LinkedList<>();
        seats.add(seat);
        Customer customer = newCustomer();

        Reservation reservation = newReservation(customer, performance, seats);
        reservation.setId(RESERVATION_TEST_ID_1);
        reservation.setReservationNumber(RESERVATION_NUMBER_TEST_1);
        reservation.setPaid(false);

        when(reservationRepositoryMock.findByPaidFalseAndId(RESERVATION_TEST_ID_1))
            .thenReturn(
                Reservation.Builder.aReservation()
                    .withId(RESERVATION_TEST_ID_1)
                    .withSeats(List.of(seat))
                    .withReservationNumber(reservation.getReservationNumber())
                    .withPerformance(performance)
                    .withCustomer(customer)
                    .withPaid(false)
                    .withPaidAt(null)
                    .build()
            );
        when(performanceRepositoryMock.findById(PERFORMANCE_TEST_ID)).thenReturn(Optional.of(performance));
        when(seatsServiceMock.createSeats(seats)).thenReturn(seats);
        when(reservationRepositoryMock.save(reservation)).thenReturn(
            Reservation.Builder.aReservation()
                .withId(RESERVATION_TEST_ID_1)
                .withSeats(new LinkedList<>())
                .withReservationNumber(reservation.getReservationNumber())
                .withPerformance(performance)
                .withCustomer(customer)
                .withPaid(false)
                .withPaidAt(null)
                .build()
        );
        seats.clear();
        reservation.setSeats(seats);
        var updatedReservation = reservationService.editReservation(reservation);
        assertEquals(0, updatedReservation.getSeats().size());
    }

    @Test(expected = InternalReservationException.class)
    public void tryToAddAlreadyReservedSeatToReservationShouldThrowInvalidReservationException() throws InternalReservationException {
        Performance performance = newPerformance();
        performance.setId(PERFORMANCE_TEST_ID);
        Seat seat = newSeat();
        List<Seat> seats = new LinkedList<>();
        seats.add(seat);
        Customer customer = newCustomer();

        Reservation reservation = newReservation(customer, performance, seats);
        reservation.setId(RESERVATION_TEST_ID_1);
        reservation.setReservationNumber(RESERVATION_NUMBER_TEST_1);
        reservation.setPaid(false);

        Seat newSeat = Seat.SeatBuilder.aSeat()
            .withSector(testSector)
            .withPositionX(seat.getPositionX())
            .withPositionY(seat.getPositionY())
            .build();
        Reservation anotherReservation = newReservation(customer, performance,
            List.of(newSeat)
        );
        anotherReservation.setId(RESERVATION_TEST_ID_2);
        anotherReservation.setReservationNumber(RESERVATION_NUMBER_TEST_2);
        anotherReservation.setPaid(false);

        when(reservationRepositoryMock.findByPaidFalseAndId(RESERVATION_TEST_ID_1))
            .thenReturn(
                Reservation.Builder.aReservation()
                    .withId(RESERVATION_TEST_ID_1)
                    .withSeats(List.of(seat))
                    .withReservationNumber(reservation.getReservationNumber())
                    .withPerformance(performance)
                    .withCustomer(customer)
                    .withPaid(false)
                    .withPaidAt(null)
                    .build()
            );
        when(reservationRepositoryMock.findByPaidFalseAndId(RESERVATION_TEST_ID_2))
            .thenReturn(
                Reservation.Builder.aReservation()
                    .withId(RESERVATION_TEST_ID_2)
                    .withSeats(new LinkedList<>())
                    .withReservationNumber(anotherReservation.getReservationNumber())
                    .withPerformance(performance)
                    .withCustomer(customer)
                    .withPaid(false)
                    .withPaidAt(null)
                    .build()
            );
        when(performanceRepositoryMock.findById(PERFORMANCE_TEST_ID)).thenReturn(Optional.of(performance));
        when(seatsServiceMock.createSeats(seats)).thenReturn(seats);
        when(reservationRepositoryMock.save(reservation)).thenReturn(
            Reservation.Builder.aReservation()
                .withId(RESERVATION_TEST_ID_2)
                .withSeats(new LinkedList<>())
                .withReservationNumber(reservation.getReservationNumber())
                .withPerformance(performance)
                .withCustomer(customer)
                .withPaid(false)
                .withPaidAt(null)
                .build()
        );
        List<Reservation> reservations = new LinkedList<>();
        reservations.add(reservation);
        reservations.add(anotherReservation);
        when(reservationRepositoryMock.findAllByPerformanceId(PERFORMANCE_TEST_ID))
            .thenReturn(reservations);

        reservation.setSeats(seats);
        var updatedReservation = reservationService.editReservation(anotherReservation);
    }

    @Test
    public void addAndRemoveSeatsToAnExistingReservation() throws InternalReservationException {
        Performance performance = newPerformance();
        performance.setId(PERFORMANCE_TEST_ID);
        Seat seat = newSeat();
        List<Seat> seats = new LinkedList<>();
        seats.add(seat);
        Customer customer = newCustomer();

        Reservation reservation = newReservation(customer, performance, seats);
        reservation.setId(RESERVATION_TEST_ID_1);
        reservation.setReservationNumber(RESERVATION_NUMBER_TEST_1);
        reservation.setPaid(false);

        when(reservationRepositoryMock.findByPaidFalseAndId(RESERVATION_TEST_ID_1))
            .thenReturn(
                Reservation.Builder.aReservation()
                    .withId(RESERVATION_TEST_ID_1)
                    .withSeats(List.of(seat))
                    .withReservationNumber(reservation.getReservationNumber())
                    .withPerformance(performance)
                    .withCustomer(customer)
                    .withPaid(false)
                    .withPaidAt(null)
                    .build()
            );
        when(performanceRepositoryMock.findById(PERFORMANCE_TEST_ID)).thenReturn(Optional.of(performance));
        when(seatsServiceMock.createSeats(seats)).thenReturn(seats);
        Seat newSeat = SEAT_TEST;
        newSeat.setSector(testSector);
        when(reservationRepositoryMock.save(reservation)).thenReturn(
            Reservation.Builder.aReservation()
                .withId(RESERVATION_TEST_ID_1)
                .withSeats(List.of(newSeat))
                .withReservationNumber(reservation.getReservationNumber())
                .withPerformance(performance)
                .withCustomer(customer)
                .withPaid(false)
                .withPaidAt(null)
                .build()
        );
        seats.remove(0);
        seats.add(newSeat);
        reservation.setSeats(seats);
        var updatedReservation = reservationService.editReservation(reservation);
        assertEquals(1, updatedReservation.getSeats().size());
        assertEquals(seats.get(0), updatedReservation.getSeats().get(0));
    }

    @Test
    public void findExistingReservationWithCustomerAndPerformance() {
        Performance performance = newPerformance();
        performance.setId(PERFORMANCE_TEST_ID);
        Seat seat = newSeat();
        List<Seat> seats = new LinkedList<>();
        seats.add(seat);
        Customer customer = newCustomer();

        Reservation reservation = newReservation(customer, performance, seats);
        reservation.setId(RESERVATION_TEST_ID_1);
        reservation.setReservationNumber(RESERVATION_NUMBER_TEST_1);
        reservation.setPaid(false);

        when(reservationRepositoryMock.findAllByCustomerNameAndPerformanceName(
            customer.getFirstName(), customer.getLastName(), performance.getName(),
            PageRequest.of(0, 10)
        )).thenReturn(new PageImpl<>(List.of(reservation)));

        List<Reservation> foundReservations = reservationService.findAllByCustomerNameAndPerformanceName(
            ReservationSearch.Builder.aReservationSearch()
                .withPerfomanceName(performance.getName())
                .withFirstName(customer.getFirstName())
                .withLastName(customer.getLastName())
                .build(),
            PageRequest.of(0, 10)
        ).getContent();

        assertEquals(1, foundReservations.size());
        Reservation foundReservation = foundReservations.get(0);
        assertEquals(reservation.getId(), foundReservation.getId());
    }

    private Reservation newReservation(Customer customer, Performance performance, List<Seat> seats) {
        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setPerformance(performance);
        reservation.setSeats(seats);
        return reservation;
    }


    private Performance newPerformance() {
        Performance performance = new Performance();
        performance.setId(1L);
        performance.setName("test");
        performance.setPrice(100L);
        performance.setPerformanceStart(LocalDateTime.now().plusYears(10));
        performance.setDuration(Duration.ofMinutes(20));
        performance.setHall(newHall());

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
        seat.setId(SEAT_TEST_ID_1);
        seat.setPositionX(1);
        seat.setPositionY(2);
        seat.setSector(testSector);
        return seat;
    }

    private Customer newCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("first name");
        customer.setLastName("last name");
        customer.setEmail("email@mail.com");
        customer.setTelephoneNumber("0123456789");
        customer.setId(1L);


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
        sector.setId(1L);
        sector.setCategory(newSectorCategory());
        sector.setSeatsPerRow(10);
        sector.setRows(3);
        sector.setStartPositionY(0);
        testSector = sector;
        return sector;
    }

    private Hall newHall() {
        Hall hall = new Hall();
        hall.setName("hall1");
        hall.setSectors(List.of(newSector()));
        hall.setAddress(newLocationAddress());
        return hall;
    }

    private SectorCategory newSectorCategory() {
        final var sectorCategory = new SectorCategory();
        sectorCategory.setId(1L);
        sectorCategory.setName("test");
        sectorCategory.setBasePriceMod(100L);
        return sectorCategory;
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
