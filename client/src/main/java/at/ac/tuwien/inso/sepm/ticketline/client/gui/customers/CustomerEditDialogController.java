package at.ac.tuwien.inso.sepm.ticketline.client.gui.customers;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.address.AddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
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

    public CustomerEditDialogController(CustomerController customerController, CustomerService customerService) {
        this.customerController = customerController;
        this.customerService = customerService;
    }

    @FXML
    private void onClickSaveCustomerButton(ActionEvent actionEvent) {
        LOGGER.debug("Clicked save customer button");

        CustomerDTO customerDTO = new CustomerDTO();

        customerDTO.setFirstName(firstNameTextfield.getText());
        customerDTO.setLastName(lastNameTextfield.getText());
        customerDTO.setTelephoneNumber(telephoneNumberTextfield.getText());
        customerDTO.setEmail(emailTextfield.getText());

        AddressDTO addressDTO = new AddressDTO();

        addressDTO.setStreet(streetTextfield.getText());
        addressDTO.setPostalCode(postalCodeTextfield.getText());
        addressDTO.setCity(cityTextfield.getText());
        addressDTO.setCountry(countryTextfield.getText());

        customerDTO.setAddress(addressDTO);

        // ToDo: Validation

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

        customerController.loadCustomerTable(CustomerController.FIRST_CUSTOMER_TABLE_PAGE);

        LOGGER.debug("Customer creation successfully completed!");

        JavaFXUtils.createInformationDialog(
            BundleManager.getBundle().getString("customers.dialog.create.dialog.success.title"),
            BundleManager.getBundle().getString("customers.dialog.create.dialog.success.header_text"),
            BundleManager.getBundle().getString("customers.dialog.create.dialog.success.content_text"),
            firstNameTextfield.getScene().getWindow()
        ).showAndWait();

        ((Stage)firstNameTextfield.getScene().getWindow()).close();
    }
}
