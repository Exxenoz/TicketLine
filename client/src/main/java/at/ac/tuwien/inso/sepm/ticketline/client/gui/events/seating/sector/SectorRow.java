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

    }

    public void setPriceLabelText(String text) {

    }

    public void setSpinnerAmount() {

    }
}

