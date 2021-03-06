package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ReservationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SeatRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Collections.singletonList;

@Profile("generateData")
@Component
public class ReservationDataGenerator implements DataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_RESERVATIONS_PER_PERFORMANCE_TO_GENERATE = 100;

    private final ReservationRepository reservationRepository;
    private final PerformanceRepository performanceRepository;
    private final SeatRepository seatRepository;
    private final CustomerRepository customerRepository;
    private final ReservationService reservationService;
    private final Faker faker;

    public ReservationDataGenerator(ReservationRepository reservationRepository, SeatRepository seatRepository, PerformanceRepository performanceRepository, CustomerRepository customerRepository, ReservationService reservationService) {
        this.reservationRepository = reservationRepository;
        this.performanceRepository = performanceRepository;
        this.seatRepository = seatRepository;
        this.customerRepository = customerRepository;
        this.reservationService = reservationService;
        faker = new Faker();
    }

    private LocalDate getRandomLocalDateForCurrentYear() {
        long minDay = LocalDate.of(LocalDate.now().getYear(), 1, 1).toEpochDay();
        long maxDay = LocalDate.of(LocalDate.now().getYear(), 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    @Override
    public void generate() {
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
            List<Customer> customers = customerRepository.findAll();
            List<Seat> seats = seatRepository.findAll();

            LOGGER.info("Generating {} reservation entries", (NUMBER_OF_RESERVATIONS_PER_PERFORMANCE_TO_GENERATE * performances.size()));

            int seatIndex = 0;
            for (int p = 0; p < performances.size(); p++) {
                Performance performance = performances.get(p);
                for (int i = 0; i < NUMBER_OF_RESERVATIONS_PER_PERFORMANCE_TO_GENERATE && seatIndex < seats.size(); i++) {
                    final var reservation = new Reservation();
                    reservation.setPerformance(performance);
                    reservation.setSeats(singletonList(seats.get(seatIndex++)));
                    reservation.setCustomer(customers.get(faker.random().nextInt(0, customers.size() - 1)));
                    reservation.setReservationNumber(reservationService.generateReservationNumber());
                    if (faker.random().nextBoolean()) {
                        reservation.setPaid(true);
                        reservation.setPaidAt(LocalDateTime.of(getRandomLocalDateForCurrentYear(), LocalTime.now()));
                        reservation.setElusivePrice(reservationService.calculatePrice(reservation));
                    }
                    else {
                        reservation.setPaid(false);
                    }
                    LOGGER.debug("Saving reservation {}", reservation);
                    reservationRepository.save(reservation);
                }
            }
        }
    }
}
