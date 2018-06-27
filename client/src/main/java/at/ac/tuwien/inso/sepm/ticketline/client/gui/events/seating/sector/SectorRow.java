package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.sector;

import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorDTO;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;

public class SectorRow {

    private Label sectorLabel;

    private Label priceLabel;

    private Spinner<Integer> seatsSpinner;

    private SectorDTO sectorDTO;

    public SectorRow(SectorDTO sectorDTO, Label sectorLabel, Label priceLabel, Spinner seatsSpinner) {
        this.sectorDTO = sectorDTO;
        this.sectorLabel = sectorLabel;
        this.priceLabel = priceLabel;
        this.seatsSpinner = seatsSpinner;
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
}

