package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.booking;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.InvoiceFileException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.InvoiceService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.CreateReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PriceUtils;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static javafx.scene.control.ButtonType.OK;

@Component
public class PurchaseReservationSummaryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    public Label performancePrice;

    @FXML
    public Label eventName;

    @FXML
    public Label performanceName;

    @FXML
    public Label ticketsNr;

    @FXML
    public Label performanceHeader;

    @FXML
    public Label customerName;

    @FXML
    public Label performanceDate;

    @FXML
    public Button backButton;

    @FXML
    public Button cancelButton;

    @FXML
    public Button buyButton;

    @FXML
    public Button printButton;

    private Stage stage;
    private ReservationDTO reservation;
    private final ReservationService reservationService;
    private boolean isReservation = false;
    private boolean showDetails = false;

    private final InvoiceService invoiceService;
    private final HallPlanController hallPlanController;
    private final SpringFxmlLoader fxmlLoader;

    PurchaseReservationSummaryController(
        SpringFxmlLoader fxmlLoader,
        ReservationService reservationService,
        InvoiceService invoiceService,
        @Lazy HallPlanController hallPlanController
    ) {
        this.fxmlLoader = fxmlLoader;
        this.reservationService = reservationService;
        this.hallPlanController = hallPlanController;
        this.invoiceService = invoiceService;
    }

    @FXML
    private void initialize() {
        LOGGER.info("Initialize PurchaseReservationSummaryController");

        ButtonBar.setButtonUniformSize(backButton, false);
        ButtonBar.setButtonUniformSize(cancelButton, false);
        ButtonBar.setButtonUniformSize(buyButton, false);
        ButtonBar.setButtonUniformSize(printButton, false);

        eventName.setText(reservation.getPerformance().getEvent().getName());
        performanceName.setText(reservation.getPerformance().getName());
        String totalAmountTickets = "" + reservation.getSeats().size();
        ticketsNr.setText(totalAmountTickets);
        customerName.setText(reservation.getCustomer().getFirstName() + " " + reservation.getCustomer().getLastName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        performanceDate.setText(reservation.getPerformance().getPerformanceStart().format(formatter));
        performancePrice.setText(PriceUtils.priceToRepresentation(reservationService.calculateCompletePrice(reservation.getSeats(), reservation.getPerformance())));
        printButton.setDisable(true);
        printButton.setVisible(false);
        printButton.setManaged(false);

        if (isReservation) {
            performanceHeader.setText(BundleManager.getBundle().getString("bookings.purchase.reservation_summary"));
            if (showDetails) {
                buyButton.setText(BundleManager.getBundle().getString("bookings.purchase.buy"));
            } else {
                buyButton.setText(BundleManager.getBundle().getString("bookings.purchase.continue"));
            }
            printButton.setDisable(true);
            printButton.setVisible(false);
            printButton.setManaged(false);
        }

        if (showDetails) {
            performanceHeader.setText(BundleManager.getBundle().getString("bookings.purchase.reservation_overview"));
            //make sure the reservation is still unpaid
            if (!reservation.isPaid() && !reservation.isCanceled()) {
                cancelButton.setText(BundleManager.getBundle().getString("bookings.purchase.edit_reservation"));
                printButton.setDisable(true);
                printButton.setVisible(false);
                printButton.setManaged(false);
            } else {
                cancelButton.setDisable(true);
                cancelButton.setVisible(false);
                cancelButton.setManaged(false);
                buyButton.setText(BundleManager.getBundle().getString("bookings.purchase.buy"));
                buyButton.setDisable(true);
                buyButton.setVisible(false);
                buyButton.setManaged(false);
                printButton.setDisable(false);
                printButton.setVisible(true);
                printButton.setManaged(true);
            }
        }
    }

    private void printInvoiceDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(BundleManager.getBundle().getString("bookings.purchase.print.title"));
        alert.setHeaderText(BundleManager.getBundle().getString("bookings.purchase.print.text"));
        ButtonType buttonTypeYes = new ButtonType(
            BundleManager.getBundle().getString("bookings.purchase.print.yes")
        );
        ButtonType buttonTypeCancel = new ButtonType(
            BundleManager.getBundle().getString("bookings.purchase.print.no"),
            ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            LOGGER.info("User wants to print invoice");
            handleInvoice(reservation);
        } else {
            LOGGER.info("User does not want to print invoice");
        }
        closeWindow();
    }

    private void handleInvoice(ReservationDTO reservationDTO) {
        try {
            if (!reservationDTO.isCanceled()) {
                invoiceService.createAndStoreInvoice(reservationDTO.getReservationNumber());
            } else {
                invoiceService.createAndStoreCancellationInvoice(reservationDTO.getReservationNumber());
            }
            invoiceService.openInvoice(reservationDTO.getReservationNumber());

        } catch (DataAccessException d) {
            LOGGER.error("An Error occurred whilst handling the file: {}", d.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, BundleManager.getExceptionBundle().getString("exception.invoice.create"), OK);
            alert.showAndWait();
        } catch (InvoiceFileException i) {
            LOGGER.error("An error occured while trying to store the file: {}", i.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, BundleManager.getExceptionBundle().getString("exception.invoice.file"), OK);
            alert.showAndWait();
        }
    }

    @FXML
    public void openPDFFileAction() {
        LOGGER.info("User clicked the open invoice button");
        handleInvoice(reservation);
    }

    public void buyTicketsButton(ActionEvent event) {
        LOGGER.info("User clicked the buy button");
        CreateReservationDTO createReservationDTO = new CreateReservationDTO();
        createReservationDTO.setCustomerID(reservation.getCustomer() != null ? reservation.getCustomer().getId() : null);
        createReservationDTO.setPerformanceID(reservation.getPerformance().getId());

        List<SeatDTO> seatDTOS = new LinkedList<>();
        seatDTOS.addAll(reservation.getSeats());
        createReservationDTO.setSeats(seatDTOS);

        //only reserve tickets
        if (!showDetails && isReservation) {
            LOGGER.debug("Reserve chosen Seats");
            try {
                ReservationDTO reservationDTO = reservationService.createNewReservation(createReservationDTO);
                TextArea textArea = new TextArea(BundleManager.getBundle().getString(
                    "bookings.purchase.reservation.endtext") + reservationDTO.getReservationNumber());
                textArea.setEditable(false);
                textArea.setMaxSize(300, 100);
                textArea.setWrapText(true);

                GridPane gridPane = new GridPane();
                gridPane.add(textArea, 0, 0);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(BundleManager.getBundle().getString("bookings.purchase.reservationnumber"));
                alert.getDialogPane().setContent(gridPane);
                alert.showAndWait();
                closeWindow();
            } catch (DataAccessException d) {
                JavaFXUtils.createErrorDialog(BundleManager.getExceptionBundle().getString("exception.reservation.general"),
                    stage.getScene().getWindow()).showAndWait();
            }
            //reserve and buy tickets
        } else if (!showDetails) {
            LOGGER.debug("Reserve and buy chosen Seats");
            try {
                reservation = reservationService.createAndPayReservation(createReservationDTO);
                printInvoiceDialog();
            } catch (DataAccessException d) {
                JavaFXUtils.createErrorDialog(BundleManager.getExceptionBundle().getString("exception.reservation.general"),
                    stage.getScene().getWindow()).showAndWait();
            }
        } else {
            LOGGER.debug("Buy reserved Seats");
            //buy already reserved tickets

            try {
                reservation = reservationService.purchaseReservation(reservation);
                printInvoiceDialog();
            } catch (DataAccessException d) {
                JavaFXUtils.createErrorDialog(BundleManager.getExceptionBundle().getString("exception.reservation.general"),
                    stage.getScene().getWindow()).showAndWait();
            }
        }
    }

    @FXML
    public void cancelButton(ActionEvent event) {
        LOGGER.info("User clicked the cancel button");
        if (showDetails) {
            hallPlanController.changeReservationDetails(reservation, stage);
            Parent parent = fxmlLoader.load("/fxml/events/book/hallPlanView.fxml");
            stage.setScene(new Scene(parent));
        } else {
            closeWindow();
        }
    }

    public void backButton(ActionEvent event) {
        LOGGER.info("User clicked the back button");
        if (showDetails) {
            closeWindow();
        } else {
            Parent parent = fxmlLoader.load("/fxml/events/book/selectCustomerView.fxml");
            stage.setScene(new Scene(parent));
            stage.setTitle(BundleManager.getBundle().getString("bookings.purchase.customer.details.title"));
            stage.centerOnScreen();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) performancePrice.getScene().getWindow();
        stage.close();
    }

    public void fill(ReservationDTO reservation, boolean isReservation, Stage stage) {
        this.showDetails = false;
        this.reservation = reservation;
        this.isReservation = isReservation;
        this.stage = stage;
    }

    public void showReservationDetails(ReservationDTO reservation, Stage stage) {
        this.reservation = reservation;
        this.stage = stage;
        this.showDetails = true;
    }
}
