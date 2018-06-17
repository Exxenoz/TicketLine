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

    @ApiModelProperty(readOnly = true, name= "The grid start x position of this sector. Starting at top left corner.")
    private int startPositionX;

    @ApiModelProperty(readOnly = true, name= "The grid start y position of this sector. Starting at top left corner.")
    private int startPositionY;

    @ApiModelProperty(readOnly = true, name= "The number of seats per row")
    private int seatsPerRow;

    @ApiModelProperty(readOnly = true, name= "The number of rows")
    private int rows;

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

    public SectorCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(SectorCategoryDTO category) {
        this.category = category;
    }
//kleinesgackiwarda
    @Override
    public String toString() {
        return "SectorDTO{" +
            "id=" + id +
            ", category=" + category +
            ", startPositionX=" + startPositionX +
            ", startPositionY=" + startPositionY +
            ", seatsPerRow=" + seatsPerRow +
            ", rows=" + rows +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SectorDTO)) return false;
        SectorDTO sectorDTO = (SectorDTO) o;
        return getStartPositionX() == sectorDTO.getStartPositionX() &&
            getStartPositionY() == sectorDTO.getStartPositionY() &&
            getSeatsPerRow() == sectorDTO.getSeatsPerRow() &&
            getRows() == sectorDTO.getRows() &&
            Objects.equals(getId(), sectorDTO.getId()) &&
            Objects.equals(getCategory(), sectorDTO.getCategory());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getCategory(), getStartPositionX(), getStartPositionY(), getSeatsPerRow(), getRows());
    }
}