package at.ac.tuwien.inso.sepm.ticketline.server.unittests.reservation;


import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
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

    public void setUp() {

    }

    @Test(expected = NotFoundException.class)
    public void purchaseReservationWithId() {
        var reservation = reservationService.findOneNotPaidReservationById(1L);
        reservationService.purchaseReservation(reservation);

        reservationService.findOneNotPaidReservationById(1L);
    }

    @Test(expected = NotFoundException.class)
    public void purchaseReservationWithCostumerName() {
        var customer = customerService.findOneById(1L);
        var reservations = reservationService.findAllNotPaidReservationsByCustomerName(customer);
        for (Reservation reservation : reservations) {
            reservationService.purchaseReservation(reservation);
        }

        reservationService.findAllNotPaidReservationsByCustomerName(customer);
    }
}
