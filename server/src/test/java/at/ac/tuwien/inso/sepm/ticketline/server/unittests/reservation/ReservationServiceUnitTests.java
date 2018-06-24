package at.ac.tuwien.inso.sepm.ticketline.server.unittests.reservation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalCancelationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalSeatReservationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InvalidReservationException;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
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
    public void createReservation() throws InvalidReservationException, InternalSeatReservationException {
        Performance performance = newPerformance();
        Seat seat = newSeat();
        Customer customer = newCustomer();

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setPerformance(performance);
        reservation.setSeats(List.of(seat));
        //  reservation.setReservationNumber("000001");

        when(reservationRepositoryMock.save(reservation)).thenReturn(newReservation(customer, performance, List.of(seat)));
        when(performanceRepositoryMock.findById(performance.getId())).thenReturn(Optional.of(newPerformance()));
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
    public void cancel() throws InternalCancelationException, InternalSeatReservationException, InvalidReservationException {
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
        performance.setPerformanceStart(LocalDateTime.of(2018, Month.APRIL, 21, 1, 1));
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
        seat.setId(1L);
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
