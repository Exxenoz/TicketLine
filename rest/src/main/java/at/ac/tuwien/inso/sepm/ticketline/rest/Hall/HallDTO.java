package at.ac.tuwien.inso.sepm.ticketline.rest.Hall;

import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorDTO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class HallDTO {

    @ApiModelProperty(required = true, readOnly = true, name = "List of hall sectors")
    private List<SectorDTO> sectors;

    public List<SectorDTO> getSectors() {
        return sectors;
    }

    public void setSectors(List<SectorDTO> sectors) {
        this.sectors = sectors;
    }
}
