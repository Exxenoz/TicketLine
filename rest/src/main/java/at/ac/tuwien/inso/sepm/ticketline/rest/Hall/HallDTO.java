package at.ac.tuwien.inso.sepm.ticketline.rest.Hall;

import at.ac.tuwien.inso.sepm.ticketline.rest.address.LocationAddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorDTO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class HallDTO {

    @ApiModelProperty(required = true, readOnly = true, name = "The ID of the hall")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The name of the hall")
    private String name;

    @ApiModelProperty(required = true, readOnly = true, name = "List of hall sectors")
    private List<SectorDTO> sectors;

    @ApiModelProperty(required = true, readOnly = true, name = "The address of the hall")
    private LocationAddressDTO address;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SectorDTO> getSectors() {
        return sectors;
    }

    public void setSectors(List<SectorDTO> sectors) {
        this.sectors = sectors;
    }
}
