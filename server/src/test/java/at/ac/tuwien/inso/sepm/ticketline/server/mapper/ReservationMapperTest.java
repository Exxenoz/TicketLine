package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.CreateReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.reservation.ReservationMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
public class ReservationMapperTest {

    private static final long PERFORMANCE_ID = 2L;
    private static final Seat SEAT_1 = Seat.builder().id(1L).build();
    private static final Seat SEAT_2 = Seat.builder().id(2L).build();
    private static final Seat SEAT_3 = Seat.builder().id(3L).build();

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class ReservationMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private ReservationMapper reservationMapper;

    @Test
    public void createReservationDTOMapsToReservationWithIds() {
        // GIVEN
        CreateReservationDTO createReservationDTO = new CreateReservationDTO();
        createReservationDTO.setPaid(true);
        createReservationDTO.setPerformanceID(PERFORMANCE_ID);
        createReservationDTO.setSeatIDs(List.of(SEAT_1.getId(), SEAT_2.getId(), SEAT_3.getId()));

        // WHEN
        Reservation reservation = reservationMapper.createReservationDTOToReservation(createReservationDTO);

        // THEN
        assertThat(reservation.getId(), is(nullValue()));
        assertThat(reservation.isPaid(), is(true));
        assertThat(reservation.getPerformance(), is(notNullValue()));
        assertThat(reservation.getPerformance().getId(), is(PERFORMANCE_ID));
        assertThat(reservation.getSeats(), is(notNullValue()));
        assertThat(reservation.getSeats(), hasItems(SEAT_1, SEAT_2, SEAT_3));
    }

}