package at.ac.tuwien.inso.sepm.ticketline.server.integrationtests;

import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.CreateReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.reservation.ReservationMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtests.base.BaseIT;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ReservationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SeatRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReservationIT extends BaseIT {

    private static final String RESERVATION_ENDPOINT = "/reservation";

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


    @Test
    public void purchaseReservationAsUser() {
        Performance performance = performanceRepository.save(newPerformance());
        Seat seat = seatRepository.save(newSeat());
        Customer customer = customerRepository.save(newCustomer());
        performanceRepository.flush();
        seatRepository.flush();
        customerRepository.flush();

        var reservation = Reservation.Builder.aReservation()
            .withCustomer(customer)
            .withPerformance(performance)
            .withSeats(singletonList(seat))
            .withPaid(false)
            .build();

        reservation = reservationRepository.save(reservation);

        reservationRepository.findByPaidFalseAndId(reservation.getId());

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

    private Performance newPerformance() {
        Performance performance = new Performance();
        performance.setName("test");
        performance.setPrice(new BigDecimal("1.00"));
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
