package at.ac.tuwien.inso.sepm.ticketline.rest.Hall;

import at.ac.tuwien.inso.sepm.ticketline.rest.address.LocationAddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorDTO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HallDTO)) return false;
        HallDTO hallDTO = (HallDTO) o;
        return Objects.equals(getId(), hallDTO.getId()) &&
            Objects.equals(getName(), hallDTO.getName()) &&
            Objects.equals(getSectors(), hallDTO.getSectors()) &&
            Objects.equals(address, hallDTO.address);
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getName(), getSectors(), address);
    }

    @Override
    public String toString() {
        return "HallDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", sectors=" + sectors +
            ", address=" + address +
            '}';
    }
}
