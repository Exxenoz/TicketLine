package at.ac.tuwien.inso.sepm.ticketline.server.unittests.reservation;


import at.ac.tuwien.inso.sepm.ticketline.rest.exception.ReservationSearchValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationSearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.ReservationSearchValidator;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static at.ac.tuwien.inso.sepm.ticketline.rest.validator.ReservationSearchValidator.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("unit-test")
public class ReservationSearchValidatorTest {

    @Test(expected = ReservationSearchValidationException.class)
    public void reservationDTOValidatorTest() throws ReservationSearchValidationException {
        ReservationSearchDTO reservationSearchDTO = null;
        validateDTO(reservationSearchDTO);
    }


    @Test
    public void validateCustomerFirstNameTest() throws ReservationSearchValidationException {
        ReservationSearchDTO reservationSearchDTO = new ReservationSearchDTO();
        reservationSearchDTO.setFirstName("Susi");
        validateCustomerFirstName(reservationSearchDTO);
    }

    @Test(expected = ReservationSearchValidationException.class)
    public void validateCustomerFirstNameNullTest() throws ReservationSearchValidationException {
        ReservationSearchDTO reservationSearchDTO = new ReservationSearchDTO();
        validateCustomerFirstName(reservationSearchDTO);
    }

    @Test(expected = ReservationSearchValidationException.class)
    public void validateCustomerFirstNameTooLongTest() throws ReservationSearchValidationException {
        ReservationSearchDTO reservationSearchDTO = new ReservationSearchDTO();
        reservationSearchDTO.setFirstName("s");
        validateCustomerFirstName(reservationSearchDTO);
    }

    @Test(expected = ReservationSearchValidationException.class)
    public void validateCustomerFirstNameTooShortTest() throws ReservationSearchValidationException {
        ReservationSearchDTO reservationSearchDTO = new ReservationSearchDTO();
        reservationSearchDTO.setFirstName("SusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusi");
        validateCustomerFirstName(reservationSearchDTO);
    }

    @Test(expected = ReservationSearchValidationException.class)
    public void validateCustomerFirstNameInvalidCharactersTest() throws ReservationSearchValidationException {
        ReservationSearchDTO reservationSearchDTO = new ReservationSearchDTO();
        reservationSearchDTO.setFirstName("Sus*");
        validateCustomerFirstName(reservationSearchDTO);
    }


    @Test
    public void validateCustomerLastNameTest() throws ReservationSearchValidationException {
        ReservationSearchDTO reservationSearchDTO = new ReservationSearchDTO();
        reservationSearchDTO.setLastName("Susi");
        validateCustomerLastName(reservationSearchDTO);
    }

    @Test(expected = ReservationSearchValidationException.class)
    public void validateCustomerLastNameNullTest() throws ReservationSearchValidationException {
        ReservationSearchDTO reservationSearchDTO = new ReservationSearchDTO();
        validateCustomerLastName(reservationSearchDTO);
    }

    @Test(expected = ReservationSearchValidationException.class)
    public void validateCustomerLastNameTooShortTest() throws ReservationSearchValidationException {
        ReservationSearchDTO reservationSearchDTO = new ReservationSearchDTO();
        reservationSearchDTO.setLastName("S");
        validateCustomerLastName(reservationSearchDTO);
    }


    @Test(expected = ReservationSearchValidationException.class)
    public void validateCustomerLastNameTooLongTest() throws ReservationSearchValidationException {
        ReservationSearchDTO reservationSearchDTO = new ReservationSearchDTO();
        reservationSearchDTO.setLastName("SusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusi");
        validateCustomerLastName(reservationSearchDTO);
    }

    @Test(expected = ReservationSearchValidationException.class)
    public void validateCustomerLastNameInvalidCharacterTest() throws ReservationSearchValidationException {
        ReservationSearchDTO reservationSearchDTO = new ReservationSearchDTO();
        reservationSearchDTO.setLastName("Sus*");
        validateCustomerLastName(reservationSearchDTO);
    }


    @Test
    public void validatePerformanceNameTest() throws ReservationSearchValidationException {
        ReservationSearchDTO reservationSearchDTO = new ReservationSearchDTO();
        reservationSearchDTO.setPerformanceName("Performance");
        validatePerformanceName(reservationSearchDTO);
    }

    @Test(expected = ReservationSearchValidationException.class)
    public void validatePerformanceNameNullTest() throws ReservationSearchValidationException {
        ReservationSearchDTO reservationSearchDTO = new ReservationSearchDTO();
        validatePerformanceName(reservationSearchDTO);
    }

    @Test(expected = ReservationSearchValidationException.class)
    public void validatePerformanceNameTooShortTest() throws ReservationSearchValidationException {
        ReservationSearchDTO reservationSearchDTO = new ReservationSearchDTO();
        reservationSearchDTO.setPerformanceName("P");
        validatePerformanceName(reservationSearchDTO);
    }

    @Test(expected = ReservationSearchValidationException.class)
    public void validatePerformanceNameTooLongTest() throws ReservationSearchValidationException {
        ReservationSearchDTO reservationSearchDTO = new ReservationSearchDTO();
        reservationSearchDTO.setPerformanceName("SusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusiSusi");
        validatePerformanceName(reservationSearchDTO);
    }

    @Test(expected = ReservationSearchValidationException.class)
    public void validatePerformanceNameInvalidCharactersTest() throws ReservationSearchValidationException {
        ReservationSearchDTO reservationSearchDTO = new ReservationSearchDTO();
        reservationSearchDTO.setPerformanceName("Per4mance");
        validatePerformanceName(reservationSearchDTO);
    }


}
