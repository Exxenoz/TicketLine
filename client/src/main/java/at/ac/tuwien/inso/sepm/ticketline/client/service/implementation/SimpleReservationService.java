package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.configuration.properties.PriceCalculationProperties;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.ReservationRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.CreateReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationSearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SimpleReservationService implements ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private PriceCalculationProperties priceCalculationProperties;

    private final ReservationRestClient reservationRestClient;

    public SimpleReservationService(ReservationRestClient reservationRestClient, PriceCalculationProperties priceCalculationProperties) {
        this.reservationRestClient = reservationRestClient;
        this.priceCalculationProperties = priceCalculationProperties;
    }

    @Override
    public List<ReservationDTO> findAllByEvent(EventDTO event) throws DataAccessException {
        if(event == null) {
            LOGGER.warn("Event not found");
            throw new DataAccessException("Event not found");
        }

        return reservationRestClient.findAllByEvent(event);
    }

    @Override
    public Long getPaidReservationCountByEvent(EventDTO event) throws DataAccessException {
        if(event == null) {
            LOGGER.warn("Event not found");
            throw new DataAccessException("Event not found");
        }

        return reservationRestClient.getPaidReservationCountByEvent(event);
    }

    @Override
    public ReservationDTO createNewReservation(CreateReservationDTO createReservationDTO) throws DataAccessException {
        return reservationRestClient.createNewReservation(createReservationDTO);
    }

    @Override
    public ReservationDTO createAndPayReservation(CreateReservationDTO createReservationDTO) throws DataAccessException {
        return reservationRestClient.createAndPayReservation(createReservationDTO);
    }

    @Override
    public ReservationDTO findOneByPaidFalseById(Long reservationId) throws DataAccessException {
        return reservationRestClient.findOneByPaidFalseById(reservationId);
    }

    @Override
    public ReservationDTO findOneByReservationNumber(String reservationNr) throws DataAccessException {
        return reservationRestClient.findOneByReservationNumber(reservationNr);
    }

    @Override
    public PageResponseDTO<ReservationDTO> findAllByCustomerNameAndPerformanceName(ReservationSearchDTO reservationSearchDTO)
        throws DataAccessException {
        return reservationRestClient.findAllByCustomerNameAndByPerformanceName(reservationSearchDTO);
    }

    @Override
    public ReservationDTO purchaseReservation(ReservationDTO reservationDTO) throws DataAccessException {
        return reservationRestClient.purchaseReservation(reservationDTO);
    }

    @Override
    public ReservationDTO editReservation(ReservationDTO reservationDTO) throws DataAccessException {
        return reservationRestClient.editReservation(reservationDTO);
    }

    @Override
    public ReservationDTO cancelReservation(Long id) throws DataAccessException {
        return reservationRestClient.cancelReservation(id);
    }

    @Override
    public PageResponseDTO<ReservationDTO> findAll(PageRequestDTO pageRequestDTO) throws DataAccessException {
        return reservationRestClient.findAll(pageRequestDTO);
    }

    @Override
    public List<ReservationDTO> findReservationsForPerformance(Long id) throws DataAccessException {
        return reservationRestClient.findReservationsForPerformance(id);
    }

    @Override
    public Long calculateCompletePrice(List<SeatDTO> seats, PerformanceDTO performanceDTO) {
        LOGGER.debug("Calculating complete Price with {} Seat(s) and {}", seats.size(), performanceDTO);
        Long price = 0L;

        //Calculate the price based on category modificator and performance base price
        for(SeatDTO s: seats) {
            price += s.getSector().getCategory().getBasePriceMod() * performanceDTO.getPrice();
        }

        price += Math.round(price * priceCalculationProperties.getSalesTax());
        return price;
    }

    @Override
    public Long calculateSinglePrice(SeatDTO seat, PerformanceDTO performanceDTO) {
        LOGGER.debug("Calculating Price with {} and {}", seat, performanceDTO);
        Long price = seat.getSector().getCategory().getBasePriceMod() * performanceDTO.getPrice();
        price += Math.round(price * priceCalculationProperties.getSalesTax());
        return price;
    }
}
