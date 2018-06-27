package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.sector;

import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorDTO;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;

import java.util.List;

public class SectorRow {

    private Label sectorLabel;

    private Label priceLabel;

    private Spinner<Integer> seatsSpinner;

    private SectorDTO sectorDTO;

    private List<SeatDTO> seats;

    public SectorRow(SectorDTO sectorDTO, Label sectorLabel, Label priceLabel, Spinner seatsSpinner) {
        this.sectorDTO = sectorDTO;
        this.sectorLabel = sectorLabel;
        this.priceLabel = priceLabel;
        this.seatsSpinner = seatsSpinner;
    }

    public SectorRow(SectorDTO sectorDTO, Label sectorLabel, Label priceLabel) {
        this.sectorLabel = sectorLabel;
        this.priceLabel = priceLabel;
        this.sectorDTO = sectorDTO;
    }

    public void setSectorLabelText(String text) {
        this.sectorLabel.setText(text);
    }

    public void setPriceLabelText(String text) {
        this.priceLabel.setText(text);
    }

    public void setSpinnerAmount(int amount) {
        this.seatsSpinner.getValueFactory().setValue(amount);
    }

    public SectorDTO getSectorDTO() {
        return sectorDTO;
    }

    public void setSectorDTO(SectorDTO sectorDTO) {
        this.sectorDTO = sectorDTO;
    }

    public List<SeatDTO> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatDTO> seats) {
        this.seats = seats;
    }

    public Spinner<Integer> getSeatsSpinner() {
        return seatsSpinner;
    }

    public void setSeatsSpinner(Spinner<Integer> seatsSpinner) {
        this.seatsSpinner = seatsSpinner;
    }
}

