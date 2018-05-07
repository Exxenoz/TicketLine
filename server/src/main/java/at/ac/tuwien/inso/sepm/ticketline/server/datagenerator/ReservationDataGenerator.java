package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SectorCategory;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ReservationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SeatRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SectorCategoryRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.List;

@Profile("generateData")
@Component
public class ReservationDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_RESERVATIONS_PER_PERFORMANCE_TO_GENERATE = 100;

    private final ReservationRepository reservationRepository;
    private final PerformanceRepository performanceRepository;
    private final SeatRepository seatRepository;
    private final Faker faker;

    public ReservationDataGenerator(ReservationRepository reservationRepository, SeatRepository seatRepository, PerformanceRepository performanceRepository) {
        this.reservationRepository = reservationRepository;
        this.performanceRepository = performanceRepository;
        this.seatRepository = seatRepository;
        faker = new Faker();
    }

    @PostConstruct
    private void generateReservations() {
        if (performanceRepository.count() == 0) {
            LOGGER.info("Could not generate reservations, because there are no performances!");
        }
        else if (seatRepository.count() == 0) {
            LOGGER.info("Could not generate reservations, because there are no seats!");
        }
        else if (reservationRepository.count() > 0) {
            LOGGER.info("Reservations already generated");
        } else {
            List<Performance> performances = performanceRepository.findAll();
            List<Seat> seats = seatRepository.findAll();

            LOGGER.info("Generating {} reservation entries", (NUMBER_OF_RESERVATIONS_PER_PERFORMANCE_TO_GENERATE * performances.size()));

            for (int p = 0; p < performances.size(); p++) {
                Performance performance = performances.get(p);
                for (int i = 0; i < NUMBER_OF_RESERVATIONS_PER_PERFORMANCE_TO_GENERATE && i < seats.size(); i++) {
                    final var reservation = new Reservation();
                    reservation.setPerformance(performance);
                    reservation.setSeat(seats.get(i));
                    LOGGER.debug("Saving reservation {}", reservation);
                    reservationRepository.save(reservation);
                }
            }
        }
    }
}
