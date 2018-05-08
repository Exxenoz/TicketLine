package at.ac.tuwien.inso.sepm.ticketline.rest.sector;

import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

@ApiModel(value = "SectorDTO", description = "The sector DTO for sector entries via rest")
public class SectorDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The category of the sector")
    private SectorCategoryDTO category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SectorCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(SectorCategoryDTO category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "SectorDTO{" +
            "id=" + id +
            ", category=" + category +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SectorDTO sectorDTO = (SectorDTO) o;

        return Objects.equals(id, sectorDTO.id) &&
            Objects.equals(category, sectorDTO.category);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, category);
    }
}
