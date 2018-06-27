package at.ac.tuwien.inso.sepm.ticketline.client.gui.customers;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.AddressValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.CustomerValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.booking.PurchaseReservationSummaryController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.validator.BaseAddressValidator;
import at.ac.tuwien.inso.sepm.ticketline.client.validator.CustomerValidator;
import at.ac.tuwien.inso.sepm.ticketline.rest.address.LocationAddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class CustomerEditDialogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    private TextField firstNameTextfield;

    @FXML
    private TextField lastNameTextfield;

    @FXML
    private TextField telephoneNumberTextfield;

    @FXML
    private TextField emailTextfield;

    @FXML
    private TextField streetTextfield;

    @FXML
    private TextField postalCodeTextfield;

    @FXML
    private TextField cityTextfield;

    @FXML
    private TextField countryTextfield;

    @FXML
    private Label firstNameErrorLabel;

    @FXML
    private Label lastNameErrorLabel;

    @FXML
    private Label telephoneNumberErrorLabel;

    @FXML
    private Label emailErrorLabel;

    @FXML
    private Label streetErrorLabel;

    @FXML
    private Label postalCodeErrorLabel;

    @FXML
    private Label cityErrorLabel;

    @FXML
    private Label countryErrorLabel;

    private final CustomerController customerController;
    private final CustomerService customerService;
    private CustomerDTO customerToEdit = null;

    private final PurchaseReservationSummaryController PRSController;
    private boolean reservationWithNewCustomer = false;
    private boolean isReservation;
    private final SpringFxmlLoader fxmlLoader;
    private ReservationDTO reservation;
    private Stage stage;

    public CustomerEditDialogController(CustomerController customerController,
                                        PurchaseReservationSummaryController PRSController,
                                        SpringFxmlLoader fxmlLoader,
                                        CustomerService customerService) {
        this.PRSController = PRSController;
        this.customerController = customerController;
        this.fxmlLoader = fxmlLoader;
        this.customerService = customerService;
    }

    @FXML
    private void initialize() {
        LOGGER.info("Initialize CustomerEditDialogController");
        // Reset customer to edit in case the whole controller object gets recycled...
        customerToEdit = null;
    }

    public void setCustomerToEdit(CustomerDTO customerToEdit) {
        this.customerToEdit = customerToEdit;

        if (customerToEdit != null) {
            firstNameTextfield.setText(customerToEdit.getFirstName());
            lastNameTextfield.setText(customerToEdit.getLastName());
            telephoneNumberTextfield.setText(customerToEdit.getTelephoneNumber());
            emailTextfield.setText(customerToEdit.getEmail());
            if (customerToEdit.getBaseAddress() != null) {
                streetTextfield.setText(customerToEdit.getBaseAddress().getStreet());
                postalCodeTextfield.setText(customerToEdit.getBaseAddress().getPostalCode());
                cityTextfield.setText(customerToEdit.getBaseAddress().getCity());
                countryTextfield.setText(customerToEdit.getBaseAddress().getCountry());
            } else {
                streetTextfield.setText("");
                postalCodeTextfield.setText("");
                cityTextfield.setText("");
                countryTextfield.setText("");
            }
        } else {
            firstNameTextfield.setText("");
            lastNameTextfield.setText("");
            telephoneNumberTextfield.setText("");
            emailTextfield.setText("");
            streetTextfield.setText("");
            postalCodeTextfield.setText("");
            cityTextfield.setText("");
            countryTextfield.setText("");
        }
    }

    @FXML
    private void onClickSaveCustomerButton(ActionEvent actionEvent) {

        LOGGER.info("User clicked save customer button");

        CustomerDTO customerDTO = new CustomerDTO();

        if (customerToEdit != null) {
            customerDTO.setId(customerToEdit.getId());
        }

        LocationAddressDTO locationAddressDTO = new LocationAddressDTO();
        customerDTO.setBaseAddress(locationAddressDTO);

        boolean valid = true;

        try {
            customerDTO.setFirstName(CustomerValidator.validateFirstName(firstNameTextfield));
            firstNameErrorLabel.setText("");
        } catch (CustomerValidationException e) {
            valid = false;
            LOGGER.warn("Customer validation failed: " + e.getMessage());
            firstNameErrorLabel.setText(e.getMessage());
        }

        try {
            customerDTO.setLastName(CustomerValidator.validateLastName(lastNameTextfield));
            lastNameErrorLabel.setText("");
        } catch (CustomerValidationException e) {
            valid = false;
            LOGGER.warn("Customer validation failed: " + e.getMessage());
            lastNameErrorLabel.setText(e.getMessage());
        }

        try {
            customerDTO.setTelephoneNumber(CustomerValidator.validateTelephoneNumber(telephoneNumberTextfield));
            telephoneNumberErrorLabel.setText("");
        } catch (CustomerValidationException e) {
            valid = false;
            LOGGER.warn("Customer validation failed: " + e.getMessage());
            telephoneNumberErrorLabel.setText(e.getMessage());
        }

        try {
            customerDTO.setEmail(CustomerValidator.validateEmail(emailTextfield));
            emailErrorLabel.setText("");
        } catch (CustomerValidationException e) {
            valid = false;
            LOGGER.warn("Customer validation failed: " + e.getMessage());
            emailErrorLabel.setText(e.getMessage());
        }

        try {
            locationAddressDTO.setStreet(BaseAddressValidator.validateStreet(streetTextfield));
            streetErrorLabel.setText("");
        } catch (AddressValidationException e) {
            valid = false;
            LOGGER.warn("Customer validation failed: " + e.getMessage());
            streetErrorLabel.setText(e.getMessage());
        }

        try {
            locationAddressDTO.setPostalCode(BaseAddressValidator.validatePostalCode(postalCodeTextfield));
            postalCodeErrorLabel.setText("");
        } catch (AddressValidationException e) {
            valid = false;
            LOGGER.warn("Customer validation failed: " + e.getMessage());
            postalCodeErrorLabel.setText(e.getMessage());
        }

        try {
            locationAddressDTO.setCity(BaseAddressValidator.validateCity(cityTextfield));
            cityErrorLabel.setText("");
        } catch (AddressValidationException e) {
            valid = false;
            LOGGER.warn("Customer validation failed: " + e.getMessage());
            cityErrorLabel.setText(e.getMessage());
        }

        try {
            locationAddressDTO.setCountry(BaseAddressValidator.validateCountry(countryTextfield));
            countryErrorLabel.setText("");
        } catch (AddressValidationException e) {
            valid = false;
            LOGGER.warn("Customer validation failed: " + e.getMessage());
            countryErrorLabel.setText(e.getMessage());
        }

        firstNameTextfield.getScene().getWindow().sizeToScene();

        if (!valid) {
            LOGGER.error("Customer data was invalid");
            return;
        }

        if (customerToEdit == null) { // Create customer
            try {
                //buy/reserve tickets for a performance with a new customer
                if (reservationWithNewCustomer) {
                    CustomerDTO newCustomer = customerService.create(customerDTO);
                    reserveWithNewCustomer(newCustomer);
                    return;
                }
                customerController.addCustomer(customerService.create(customerDTO));
            } catch (DataAccessException e) {
                LOGGER.error("Customer creation failed: " + e.getMessage());

                JavaFXUtils.createErrorDialog(
                    BundleManager.getBundle().getString("customers.dialog.create.dialog.error.title"),
                    BundleManager.getBundle().getString("customers.dialog.create.dialog.error.header_text"),
                    BundleManager.getBundle().getString("customers.dialog.create.dialog.error.content_text"),
                    firstNameTextfield.getScene().getWindow()
                ).showAndWait();

                return;
            }

            ((Stage) firstNameTextfield.getScene().getWindow()).close();

            LOGGER.debug("Customer creation successfully completed!");

            JavaFXUtils.createInformationDialog(
                BundleManager.getBundle().getString("customers.dialog.create.dialog.success.title"),
                BundleManager.getBundle().getString("customers.dialog.create.dialog.success.header_text"),
                BundleManager.getBundle().getString("customers.dialog.create.dialog.success.content_text"),
                firstNameTextfield.getScene().getWindow()
            ).showAndWait();

        } else { // Edit customer
            try {
                customerToEdit.update(customerService.update(customerDTO));
            } catch (DataAccessException e) {
                LOGGER.error("Customer editing failed: " + e.getMessage());

                JavaFXUtils.createErrorDialog(
                    BundleManager.getBundle().getString("customers.dialog.edit.dialog.error.title"),
                    BundleManager.getBundle().getString("customers.dialog.edit.dialog.error.header_text"),
                    BundleManager.getBundle().getString("customers.dialog.edit.dialog.error.content_text"),
                    firstNameTextfield.getScene().getWindow()
                ).showAndWait();

                return;
            }

            ((Stage) firstNameTextfield.getScene().getWindow()).close();

            customerController.refreshAndSortCustomerTable();

            LOGGER.debug("Customer editing successfully completed!");

            JavaFXUtils.createInformationDialog(
                BundleManager.getBundle().getString("customers.dialog.edit.dialog.success.title"),
                BundleManager.getBundle().getString("customers.dialog.edit.dialog.success.header_text"),
                BundleManager.getBundle().getString("customers.dialog.edit.dialog.success.content_text"),
                firstNameTextfield.getScene().getWindow()
            ).showAndWait();
        }
    }

    private void reserveWithNewCustomer(CustomerDTO newCustomer) {
        LOGGER.info("User clicked the reserve with customer button");
        reservation.setCustomer(newCustomer);
        PRSController.fill(reservation, isReservation, stage);
        Parent parent = fxmlLoader.load("/fxml/events/book/purchaseReservationSummary.fxml");
        stage.setScene(new Scene(parent));
        stage.centerOnScreen();
    }

    public void fill(boolean reservationWithNewCustomer, ReservationDTO reservation, boolean isReservation, Stage stage) {
        this.reservationWithNewCustomer = reservationWithNewCustomer;
        this.reservation = reservation;
        this.isReservation = isReservation;
        this.stage = stage;
    }
}
