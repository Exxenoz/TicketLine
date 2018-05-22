package at.ac.tuwien.inso.sepm.ticketline.server.unittests.reservation;


import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ReservationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SeatRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.LinkedList;

import static java.math.BigDecimal.ONE;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("unit-test")
public class ReservationServiceTests {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CustomerService customerService;

    private static Long RESERVATION_TEST_ID = 1L;
    private static Long CUSTOMER_TEST_ID = 1L;
    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Before
    public void setUp() {
        Performance performance = performanceRepository.save(newPerformance());
        Seat seat = seatRepository.save(newSeat());
        LinkedList<Seat> seats = new LinkedList<>();
        seats.add(seat);
        Customer customer = customerRepository.save(newCustomer());
        CUSTOMER_TEST_ID = customer.getId();

        Reservation reservation = new Reservation();
        reservation.setPaid(false);
        reservation.setSeats(seats);
        reservation.setPerformance(performance);
        reservation.setCustomer(customer);

        reservation = reservationRepository.save(reservation);
        RESERVATION_TEST_ID = reservation.getId();
    }


    @Test
    public void purchaseReservationWithId() {
        var reservation = reservationService.findOneByPaidFalseById(RESERVATION_TEST_ID);
        Assert.assertNotNull(reservation);
        Assert.assertEquals(false, reservation.isPaid());
        reservationService.purchaseReservation(reservation);
        Assert.assertEquals(true, reservation.isPaid());

        Assert.assertNull(reservationService.findOneByPaidFalseById(RESERVATION_TEST_ID));
    }

    @Test
    public void deleteReservationWithId() {
        var reservation = reservationService.findOneByPaidFalseById(RESERVATION_TEST_ID);
        Assert.assertNotNull(reservation);
        reservationService.deleteReservation(reservation);

        Assert.assertNull(reservationService.findOneByPaidFalseById(RESERVATION_TEST_ID));
    }

    @Test
    @Transactional
    public void deleteReservationWithCustomer() {
        var customer = customerService.findOneById(CUSTOMER_TEST_ID);
        var reservations = reservationService.findAllByPaidFalseByCustomerName(customer);
        for (Reservation reservation : reservations) {
            Assert.assertNotNull(reservation);
            reservationService.deleteReservation(reservation);
        }

        reservations = reservationService.findAllByPaidFalseByCustomerName(customer);
        Assert.assertEquals(0, reservations.size());
    }

    @Test
    @Transactional
    public void purchaseReservationWithCostumer() {
        var customer = customerService.findOneById(CUSTOMER_TEST_ID);
        var reservations = reservationService.findAllByPaidFalseByCustomerName(customer);
        for (Reservation reservation : reservations) {
            reservationService.purchaseReservation(reservation);
        }

        reservations = reservationService.findAllByPaidFalseByCustomerName(customer);
        Assert.assertEquals(0, reservations.size());
    }

    private Performance newPerformance() {
        Performance performance = new Performance();
        performance.setName("test");
        performance.setPrice(ONE);
        performance.setPerformanceStart(LocalDateTime.now());
        performance.setPerformanceEnd(LocalDateTime.now());

        Address address = new Address();
        address.setCity("city");
        address.setCountry("country");
        address.setLocationName("locationName");
        address.setStreet("street");
        address.setPostalCode("postalCode");
        performance.setAddress(address);
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
        //customer.setId(CUSTOMER_TEST_ID);


        Address address = new Address();
        address.setCity("city");
        address.setCountry("country");
        address.setLocationName("locationName");
        address.setStreet("street");
        address.setPostalCode("postalCode");
        customer.setAddress(address);

        return customer;
    }

}
