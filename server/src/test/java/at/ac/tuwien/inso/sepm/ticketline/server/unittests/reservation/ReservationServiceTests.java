package at.ac.tuwien.inso.sepm.ticketline.server.unittests.reservation;


import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("unit-test")
public class ReservationServiceTests {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DataSource dataSource;

    private static final Long RESERVATION_PURCHASE_TEST_ID = 187L;
    private static final Long RESERVATION_DELETE_TEST_ID = 2L;
    private static final Long CUSTOMER_TEST_ID = 1L;

    public void setUp() {

    }

    @Test
    public void purchaseReservationWithId() {
        var reservation = reservationService.findOneByPaidFalseById(RESERVATION_PURCHASE_TEST_ID);
        Assert.assertNotNull(reservation);
        Assert.assertEquals(false, reservation.isPaid());
        reservationService.purchaseReservation(reservation);
        Assert.assertEquals(true, reservation.isPaid());

        Assert.assertNull(reservationService.findOneByPaidFalseById(RESERVATION_PURCHASE_TEST_ID));
    }

    @Test
    public void deleteReservationWithId() {
        var reservation = reservationService.findOneByPaidFalseById(RESERVATION_DELETE_TEST_ID);
        Assert.assertNotNull(reservation);
        reservationService.deleteReservation(reservation);

        Assert.assertNull(reservationService.findOneByPaidFalseById(RESERVATION_DELETE_TEST_ID));
    }

    @Test
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
    public void purchaseReservationWithCostumer() {
        var customer = customerService.findOneById(CUSTOMER_TEST_ID);
        var reservations = reservationService.findAllByPaidFalseByCustomerName(customer);
        for (Reservation reservation : reservations) {
            reservationService.purchaseReservation(reservation);
        }

        reservationService.findAllByPaidFalseByCustomerName(customer);
    }
}
