package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.ReservationRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.CreateReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationSearchDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Component
public class SimpleReservationRestClient implements ReservationRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RestClient restClient;
    private final URI reservationByEventUri;
    private final URI reservationTopTenUri;
    private final URI reservationCreateUri;
    private final URI reservationCreateAndPayUri;
    private final URI reservationFindAllUri;
    private final URI reservationEditUri;
    private final URI reservationPurchaseUri;
    private final URI reservationFindNotPaidUri;
    private final URI reservationFindNotPaidByReservationNumberUri;

    public SimpleReservationRestClient(RestClient restClient) {
        this.restClient = restClient;
        this.reservationByEventUri = restClient.getServiceURI("/reservation/event/");
        this.reservationTopTenUri = restClient.getServiceURI("/reservation/top_ten/");
        this.reservationCreateUri = restClient.getServiceURI("/reservation/");
        this.reservationCreateAndPayUri = restClient.getServiceURI("/reservation/createAndPay/");
        this.reservationFindAllUri = restClient.getServiceURI("/reservation/findAll");
        this.reservationEditUri = restClient.getServiceURI("/reservation/edit");
        this.reservationPurchaseUri = restClient.getServiceURI("/reservation/purchase");
        this.reservationFindNotPaidUri = restClient.getServiceURI("/reservation/findNotPaid");
        this.reservationFindNotPaidByReservationNumberUri =
            restClient.getServiceURI("reservation/findNotPaid/ReservationNumber");
    }

    @Override
    public List<ReservationDTO> findAllByEvent(EventDTO event) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving all reservations of a specific event from {}", reservationByEventUri);
            final var reservation =
                restClient.exchange(
                    new RequestEntity<>(GET, reservationByEventUri.resolve(event.getId() + "")),
                    new ParameterizedTypeReference<List<ReservationDTO>>() {
                });
            LOGGER.debug("Result status was {} with content {}", reservation.getStatusCode(), reservation.getBody());
            return reservation.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()));
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public Long getPaidReservationCountByEvent(EventDTO event) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving paid reservation count of a specific event from {}", reservationByEventUri);
            final var reservation =
                restClient.exchange(
                    new RequestEntity<>(GET, reservationByEventUri.resolve(event.getId() + "/count")),
                    new ParameterizedTypeReference<Long>() {
                });
            LOGGER.debug("Result status was {} with content {}", reservation.getStatusCode(), reservation.getBody());
            return reservation.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()));
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public ReservationDTO createNewReservation(CreateReservationDTO createReservationDTO) throws DataAccessException {
        try {
            LOGGER.debug("Entering createNewReservation method with reservationCreateUri {}", reservationCreateUri);
            final var response =
                restClient.exchange(
                    new RequestEntity<>(createReservationDTO, POST, reservationCreateUri),
                    new ParameterizedTypeReference<ReservationDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", response.getStatusCode(), response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()));
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }

    }

    @Override
    public ReservationDTO createAndPayReservation(CreateReservationDTO createReservationDTO) throws DataAccessException {
        try {
            LOGGER.debug("Entering createNewReservation method with reservationCreateAndPayUri {}", reservationCreateAndPayUri);
            final var reservation =
                restClient.exchange(
                    new RequestEntity<>(createReservationDTO, POST, reservationCreateAndPayUri),
                    new ParameterizedTypeReference<ReservationDTO>() {
                    });
            return reservation.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()));
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public ReservationDTO findOneByPaidFalseById(Long reservationId) throws DataAccessException {
        try {
            LOGGER.debug("Entering findOneByPaidFalseById method with URI {}", reservationFindNotPaidUri);
            URI uri = restClient.getServiceURI(reservationFindNotPaidUri + "/" + reservationId);
            final var response =
                restClient.exchange(
                    new RequestEntity<>(POST, uri),
                    new ParameterizedTypeReference<ReservationDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", response.getStatusCode(), response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()));
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<ReservationDTO> findAllByPaidFalseByCustomerNameAndByPerformanceName(ReservationSearchDTO reservationSearchDTO) throws DataAccessException {
        try {
            LOGGER.debug("Entering findAllByPaidFalseByCustomerName method with URI {}", reservationFindNotPaidUri);
            final var response =
                restClient.exchange(
                    new RequestEntity<>(reservationSearchDTO, POST, reservationFindNotPaidUri),
                    new ParameterizedTypeReference<List<ReservationDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", response.getStatusCode(), response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()));
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public ReservationDTO purchaseReservation(ReservationDTO reservationDTO) throws DataAccessException {
        try {
            LOGGER.debug("Entering purchaseReservation method with URI {}", reservationPurchaseUri);
            final var response =
                restClient.exchange(
                    new RequestEntity<>(reservationDTO, POST, reservationPurchaseUri),
                    new ParameterizedTypeReference<ReservationDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", response.getStatusCode(), response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()));
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public ReservationDTO editReservation(ReservationDTO reservationDTO) throws DataAccessException {
        try {
            LOGGER.debug("Entering editReservation method with URI {}", reservationEditUri);
            final var response =
                restClient.exchange(
                    new RequestEntity<>(reservationDTO, POST, reservationEditUri),
                    new ParameterizedTypeReference<ReservationDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", response.getStatusCode(), response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()));
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public ReservationDTO findOneByPaidFalseAndReservationNumber(String reservationNr) throws DataAccessException {
        try {
            LOGGER.debug("Entering findOneByPaidFalseAndReservationNumber method with URI {}",
                reservationFindNotPaidByReservationNumberUri);
            final var response =
                restClient.exchange(
                    new RequestEntity<>(reservationNr, POST, reservationFindNotPaidByReservationNumberUri),
                    new ParameterizedTypeReference<ReservationDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", response.getStatusCode(), response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()));
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public PageResponseDTO<ReservationDTO> findAll(PageRequestDTO pageRequestDTO) throws DataAccessException {
        try {
            LOGGER.debug("Entering findAll method with URI {}", reservationFindAllUri);
            final var response =
                restClient.exchange(
                    new RequestEntity<>(pageRequestDTO, POST, reservationFindAllUri),
                    new ParameterizedTypeReference<PageResponseDTO<ReservationDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", response.getStatusCode(), response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()));
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
