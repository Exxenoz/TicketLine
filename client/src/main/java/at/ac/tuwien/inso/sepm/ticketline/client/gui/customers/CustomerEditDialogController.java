package at.ac.tuwien.inso.sepm.ticketline.client.gui.customers;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.address.LocationAddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.AddressValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.CustomerValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.AddressValidator;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.CustomerValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    public CustomerEditDialogController(CustomerController customerController, CustomerService customerService) {
        this.customerController = customerController;
        this.customerService = customerService;
    }

    @FXML
    private void initialize() {
        // Reset customer to edit in case the whole controller object gets recycled...
        customerToEdit = null;
    }

    public void SetCustomerToEdit(CustomerDTO customerToEdit) {
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
            }
            else {
                streetTextfield.setText("");
                postalCodeTextfield.setText("");
                cityTextfield.setText("");
                countryTextfield.setText("");
            }
        }
        else {
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
        LOGGER.debug("Clicked save customer button");

        CustomerDTO customerDTO = new CustomerDTO();

        if (customerToEdit != null) {
            customerDTO.setId(customerToEdit.getId());
        }
        customerDTO.setFirstName(firstNameTextfield.getText());
        customerDTO.setLastName(lastNameTextfield.getText());
        customerDTO.setTelephoneNumber(telephoneNumberTextfield.getText());
        customerDTO.setEmail(emailTextfield.getText());

        LocationAddressDTO locationAddressDTO = new LocationAddressDTO();

        locationAddressDTO.setStreet(streetTextfield.getText());
        locationAddressDTO.setPostalCode(postalCodeTextfield.getText());
        locationAddressDTO.setCity(cityTextfield.getText());
        locationAddressDTO.setCountry(countryTextfield.getText());

        customerDTO.setBaseAddress(locationAddressDTO);

        boolean valid = true;

        try {
            CustomerValidator.validateFirstName(customerDTO);
            firstNameErrorLabel.setText("");
        }
        catch (CustomerValidationException e) {
            valid = false;
            LOGGER.debug("Customer validation failed: " + e.getMessage());
            firstNameErrorLabel.setText(e.getMessage());
        }

        try {
            CustomerValidator.validateLastName(customerDTO);
            lastNameErrorLabel.setText("");
        }
        catch (CustomerValidationException e) {
            valid = false;
            LOGGER.debug("Customer validation failed: " + e.getMessage());
            lastNameErrorLabel.setText(e.getMessage());
        }

        try {
            CustomerValidator.validateTelephoneNumber(customerDTO);
            telephoneNumberErrorLabel.setText("");
        }
        catch (CustomerValidationException e) {
            valid = false;
            LOGGER.debug("Customer validation failed: " + e.getMessage());
            telephoneNumberErrorLabel.setText(e.getMessage());
        }

        try {
            CustomerValidator.validateEmail(customerDTO);
            emailErrorLabel.setText("");
        }
        catch (CustomerValidationException e) {
            valid = false;
            LOGGER.debug("Customer validation failed: " + e.getMessage());
            emailErrorLabel.setText(e.getMessage());
        }

        try {
            AddressValidator.validateStreet(locationAddressDTO);
            streetErrorLabel.setText("");
        }
        catch (AddressValidationException e) {
            valid = false;
            LOGGER.debug("Customer validation failed: " + e.getMessage());
            streetErrorLabel.setText(e.getMessage());
        }

        try {
            AddressValidator.validatePostalCode(locationAddressDTO);
            postalCodeErrorLabel.setText("");
        }
        catch (AddressValidationException e) {
            valid = false;
            LOGGER.debug("Customer validation failed: " + e.getMessage());
            postalCodeErrorLabel.setText(e.getMessage());
        }

        try {
            AddressValidator.validateCity(locationAddressDTO);
            cityErrorLabel.setText("");
        }
        catch (AddressValidationException e) {
            valid = false;
            LOGGER.debug("Customer validation failed: " + e.getMessage());
            cityErrorLabel.setText(e.getMessage());
        }

        try {
            AddressValidator.validateCountry(locationAddressDTO);
            countryErrorLabel.setText("");
        }
        catch (AddressValidationException e) {
            valid = false;
            LOGGER.debug("Customer validation failed: " + e.getMessage());
            countryErrorLabel.setText(e.getMessage());
        }

        if (!valid) {
            return;
        }

        if (customerToEdit == null) { // Create customer
            try {
                customerService.create(customerDTO);
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

            customerController.clearCustomerList();
            customerController.loadCustomerTable(CustomerController.FIRST_CUSTOMER_TABLE_PAGE);

            LOGGER.debug("Customer creation successfully completed!");

            JavaFXUtils.createInformationDialog(
                BundleManager.getBundle().getString("customers.dialog.create.dialog.success.title"),
                BundleManager.getBundle().getString("customers.dialog.create.dialog.success.header_text"),
                BundleManager.getBundle().getString("customers.dialog.create.dialog.success.content_text"),
                firstNameTextfield.getScene().getWindow()
            ).showAndWait();
        }
        else { // Edit customer
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

            customerController.refreshCustomerTable();

            LOGGER.debug("Customer editing successfully completed!");

            JavaFXUtils.createInformationDialog(
                BundleManager.getBundle().getString("customers.dialog.edit.dialog.success.title"),
                BundleManager.getBundle().getString("customers.dialog.edit.dialog.success.header_text"),
                BundleManager.getBundle().getString("customers.dialog.edit.dialog.success.content_text"),
                firstNameTextfield.getScene().getWindow()
            ).showAndWait();
        }

        ((Stage)firstNameTextfield.getScene().getWindow()).close();
    }
}
