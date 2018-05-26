package at.ac.tuwien.inso.sepm.ticketline.rest.sector;

import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import io.swagger.annotations.Api;
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

    @ApiModelProperty(required = false, readOnly = true, name="The grid start x position of this sector. Starting at top left corner.")
    private int startPositionX;

    @ApiModelProperty(required = false, readOnly = true, name="The grid start y position of this sector. Starting at top left corner.")
    private int startPositionY;

    @ApiModelProperty(required = false, readOnly = true, name="The number of seats per row")
    private int seatsPerRow;

    @ApiModelProperty(required = false, readOnly = true, name="The number of rows")
    private int rows;

    @ApiModelProperty
    private List<SeatDTO> seats;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStartPositionX() {
        return startPositionX;
    }

    public void setStartPositionX(int startPositionX) {
        this.startPositionX = startPositionX;
    }

    public int getStartPositionY() {
        return startPositionY;
    }

    public void setStartPositionY(int startPositionY) {
        this.startPositionY = startPositionY;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public void setSeatsPerRow(int seatsPerRow) {
        this.seatsPerRow = seatsPerRow;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public List<SeatDTO> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatDTO> seats) {
        this.seats = seats;
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
