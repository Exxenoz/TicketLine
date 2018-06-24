package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalCancelationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalHallValidationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalSeatReservationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InvalidReservationException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ReservationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.HallPlanService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.SeatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class SimpleReservationService implements ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private HallPlanService hallPlanService;
    @Autowired
    private SeatsService seatsService;

    //   private long lastReservationNumber = 16777216;

    public ReservationRepository getReservationRepository() {
        return reservationRepository;
    }

    @Override
    public void setReservationRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void setPerformanceRepository(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    @Override
    public void setSeatsService(SeatsService seatsService) {
        this.seatsService = seatsService;
    }

    @Override
    public List<Reservation> findAllByEventId(Long eventId) {
        return reservationRepository.findAllByEventId(eventId);
    }

    @Override
    public Reservation findOneByPaidFalseAndId(Long reservationId) {
        return reservationRepository.findByPaidFalseAndId(reservationId);
    }

    @Override
    public Reservation findOneByReservationNumber(String reservationNr) {
        return reservationRepository.findByReservationNumber(reservationNr);
    }

    @Override
    public Page<Reservation> findAllByCustomerNameAndPerformanceName(ReservationSearch reservationSearch,
                                                                     Pageable pageable) {
        String firstName = reservationSearch.getFirstName();
        String lastName = reservationSearch.getLastName();
        String performanceName = reservationSearch.getPerformanceName();
        return reservationRepository.findAllByCustomerNameAndPerformanceName(firstName, lastName, performanceName,
            pageable);
    }

    @Override
    public Long getPaidReservationCountByEventId(Long eventId) {
        return reservationRepository.getPaidReservationCountByEventId(eventId);
    }

    @Override
    public Reservation purchaseReservation(Reservation reservation) {
        reservation.setPaid(true);
        reservation.setPaidAt(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public Reservation createAndPayReservation(Reservation reservation) throws InvalidReservationException, InternalSeatReservationException {
        createReservation(reservation);
        return purchaseReservation(reservation);
    }

    @Override
    public Page<Reservation> findAll(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }

    @Override
    public Reservation editReservation(Reservation reservation) throws InvalidReservationException {
        List<Seat> changedSeats = reservation.getSeats();//the changed seats
        Reservation savedReservation = reservationRepository.findByPaidFalseAndId(reservation.getId());
        List<Seat> savedSeats = savedReservation.getSeats();//the old seat config
        List<Seat> onlyNewSeats = getNewSeats(changedSeats);//the not yet saved seats

        //First, get the picked performance for this reservation
        List<Sector> sectors = null;
        Performance performance = null;
        if (performanceRepository.findById(reservation.getPerformance().getId()).isPresent()) {
            performance = performanceRepository.findById(reservation.getPerformance().getId()).get();
            sectors = performance.getHall().getSectors();
        }

        //Then, too fail fast, we check the integrity of the seats according to the hall plan and the sectors
        try {
            if (sectors != null) {
                hallPlanService.checkSeatsAgainstSectors(onlyNewSeats, sectors);
            } else {
                LOGGER.error("Could not find the the Sectors for this reservation");
                throw new InvalidReservationException("The Performance was not set");
            }
        } catch (InternalHallValidationException i) {
            LOGGER.warn("The sectors of the reservation do not match the hall '{}", performance.getHall());
            throw new InvalidReservationException("Hall plan is not coherent with sectors or seats.");
        }
        //check if all new seats are free
        checkIfAllSeatsAreFreeIgnoreId(reservation.getId(), performance.getId(), onlyNewSeats);
        LOGGER.debug("The added seats are still free");

        //create the new Seats
        seatsService.createSeats(onlyNewSeats);

        //save changes
        LOGGER.debug("Update reservation");
        Reservation out = reservationRepository.save(reservation);

        //delete Seats, if they were removed from the reservation
        if (!changedSeats.containsAll(savedSeats)) {
            List<Seat> removedSeats = new LinkedList<>();
            for (Seat seat : savedSeats) {
                if (!changedSeats.contains(seat)) {
                    removedSeats.add(seat);
                }
            }
            seatsService.deleteAll(removedSeats);
            LOGGER.debug("Delete removed Seats");
        }

        return out;
    }

    private List<Seat> getNewSeats(List<Seat> seats) {
        List<Seat> newSeats = new LinkedList<>();
        for (Seat seat : seats) {
            if (seat.getId() == null) {
                newSeats.add(seat);
            }
        }
        return newSeats;
    }

    private List<Seat> getExistingSeats(List<Seat> seats) {
        List<Seat> existingSeats = new LinkedList<>();
        for (Seat seat : seats) {
            if (seat.getId() != null) {
                existingSeats.add(seat);
            }
        }
        return existingSeats;
    }


    private void checkIfAllSeatsAreFreeIgnoreId(Long reservationId, Long performanceId, List<Seat> seatsToCheck) throws InvalidReservationException {
        List<Reservation> allReservations = reservationRepository.findAllByPerformanceId(performanceId);

        for (Reservation reservation : allReservations) {
            if (!reservation.getId().equals(reservationId)) {
                for (Seat seat : seatsToCheck) {
                    for (Seat otherSeat : reservation.getSeats()) {
                        if (seat.equalsWithoutId(otherSeat)) {
                            LOGGER.warn("The seat {} is already reserved", seat);
                            throw new InvalidReservationException("Seat " + seat + " is already reserved!");
                        }
                    }
                }
            }
        }
    }

    private void checkIfAllSeatsAreFree(List<Seat> seatsToCheck) throws InvalidReservationException {
        List<Reservation> allReservations = reservationRepository.findAll();
        for (Reservation reservation : allReservations) {
            for (Seat seat : seatsToCheck) {
                if (reservation.getSeats().contains(seat)) {
                    throw new InvalidReservationException("Seats are already reserved!");
                }
            }
        }
    }

    @Override
    @Transactional
    public Reservation createReservation(Reservation reservation) throws InvalidReservationException, InternalSeatReservationException {
        //First, get the picked performance for this reservation
        Performance performance = performanceRepository.findById(reservation.getPerformance().getId()).get();

        //Then, too fail fast, we check the integrity of the seats according to the hall plan and the sectors
        try {
            List<Seat> seatst = reservation.getSeats();
            Hall hall = performance.getHall();
            List<Sector> sectort = hall.getSectors();
            hallPlanService.checkSeatsAgainstSectors(reservation.getSeats(), performance.getHall().getSectors());
        } catch (InternalHallValidationException i) {
            LOGGER.warn("The sectors of the reservation do not match the hall '{}", performance.getHall());
            throw new InvalidReservationException("Hall plan is not coherent with sectors or seats.");
        }

        //Then we check the seats against all reservations, and if they actually exist
        List<Reservation> reservations = reservationRepository.findAllByPerformanceId(reservation.getPerformance().getId());
        for(Reservation r: reservations) {
            for(Seat existingSeat: r.getSeats()) {
                for(Seat requestedSeat: reservation.getSeats()) {
                    if(requestedSeat.getSector().getId() == existingSeat.getSector().getId()
                        && requestedSeat.getPositionX() == existingSeat.getPositionX()
                        && requestedSeat.getPositionY() == existingSeat.getPositionY()) {

                        LOGGER.warn("seat '{}' is already reserved", requestedSeat);
                        throw new InternalSeatReservationException("A seat is already reserved.", requestedSeat);
                    }
                }
            }
        }
        LOGGER.debug("Seat reservation found no collisions.");

        //The seats seem to be fine and there are no reservation conflicts, now we want to actually create our seats
        List<Seat> createdSeats = seatsService.createSeats(reservation.getSeats());

        //Proceed with reservation creation, set paid status to false
        reservation.setPaid(false);
        Performance currentPerformance = performanceRepository.findById(reservation.getPerformance().getId()).get();

        Reservation createdReservation = null;

        reservation.setReservationNumber(generateReservationNumber());

        //Set all the information we need and save
        reservation.setSeats(createdSeats);
        reservation.setPerformance(currentPerformance);

        LOGGER.debug("Storing reservation");
        createdReservation = reservationRepository.save(reservation);
        return createdReservation;
    }

  /*  public String generateReservationNumber() {
        String reservationNumber = "";
        final String ALPHA_NUMERIC_STRING = "ABCDEFGHJKLMNPQRSTUVWXYZ0123456789";
        while (reservationNumber.length() <= 6) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            reservationNumber += ALPHA_NUMERIC_STRING.charAt(character);
        }
        return reservationNumber;
    } */


    public String generateReservationNumber() {
        String reservationNumber;
        Reservation previous = reservationRepository.findFirstByOrderByIdDesc();
        //if this is the first generated reservation
        if (previous == null) {
            //First HEX Number with 7 Numbers
            long first = 16777216;
            reservationNumber = Long.toHexString(first);
            reservationNumber = reservationNumber.toUpperCase();
        } else {
            String lastNumber = previous.getReservationNumber();
            Long lastNumberLong = Long.parseLong(lastNumber, 16);
            reservationNumber = Long.toHexString(++lastNumberLong);
            reservationNumber = reservationNumber.toUpperCase();
        }
        return reservationNumber;
    }


    @Override
    public Reservation cancelReservation(Long id) throws InternalCancelationException {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(InternalCancelationException::new);
        List<Seat> seatsOfReservation = reservation.getSeats();
        reservation.setSeats(null);
        reservation.setCanceled(true);
        Reservation canceledReservation = reservationRepository.save(reservation);
        seatsOfReservation.forEach(seat -> seatsService.deleteSeat(seat));

        return canceledReservation;
    }

    @Override
    public List<Reservation> findReservationsForPerformance(Long id) {
        return reservationRepository.findAllByPerformanceId(id);
    }

    @Override
    public Long calculatePrice(Reservation reservation) {
        Long performancePrice = reservation.getPerformance().getPrice();
        Long calculatedPrice = 0l;
        for(Seat s: reservation.getSeats()) {
            calculatedPrice += performancePrice * s.getSector().getCategory().getBasePriceMod();
        }

        return calculatedPrice;
    }
}
