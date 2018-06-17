package at.ac.tuwien.inso.sepm.ticketline.rest.seat;

import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(value = "SeatDTO", description = "The seat DTO for seat entries via rest")
public class SeatDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The x position of the seat in the grid")
    private Integer positionX;

    @ApiModelProperty(required = true, readOnly = true, name = "The y position of the seat in the grid")
    private Integer positionY;

    @ApiModelProperty(required = true, readOnly = true, name = "The sector of the seat")
    private SectorDTO sector;

    public SeatDTO() {}

    public SeatDTO(Integer positionX, Integer positionY, SectorDTO sector) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.sector = sector;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPositionX() {
        return positionX;
    }

    public void setPositionX(Integer positionX) {
        this.positionX = positionX;
    }

    public Integer getPositionY() {
        return positionY;
    }

    public void setPositionY(Integer positionY) {
        this.positionY = positionY;
    }

    public SectorDTO getSector() {
        return sector;
    }

    public void setSector(SectorDTO sector) {
        this.sector = sector;
    }

    @Override
    public String toString() {
        return "SeatDTO{" +
            "id=" + id +
            ", positionX=" + positionX +
            ", positionY=" + positionY +
            ", sector=" + sector +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SeatDTO seatDTO = (SeatDTO) o;

        return Objects.equals(id, seatDTO.id) &&
            Objects.equals(positionX, seatDTO.positionX) &&
            Objects.equals(positionY, seatDTO.positionY) &&
            Objects.equals(sector, seatDTO.sector);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, positionX, positionY, sector);
    }

    public static final class Builder {
        private Long id;
        private Integer positionX;
        private Integer positionY;
        private SectorDTO sector;

        private Builder() {
        }

        public static Builder aSeatDTO() {
            return new Builder();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withPositionX(Integer positionX) {
            this.positionX = positionX;
            return this;
        }

        public Builder withPositionY(Integer positionY) {
            this.positionY = positionY;
            return this;
        }

        public Builder withSector(SectorDTO sector) {
            this.sector = sector;
            return this;
        }

        public SeatDTO build() {
            SeatDTO seatDTO = new SeatDTO();
            seatDTO.setId(id);
            seatDTO.setPositionX(positionX);
            seatDTO.setPositionY(positionY);
            seatDTO.setSector(sector);
            return seatDTO;
        }
    }
}
