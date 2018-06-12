package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class Sector {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "seq_sector_id")
    @SequenceGenerator(name = "seq_sector_id", sequenceName = "seq_sector_id")
    private Long id;

    @ManyToOne
    private SectorCategory category;

    @Column(name = "start_position_x")
    private int startPositionX;

    @Column(name = "start_position_y")
    private int startPositionY;

    @Column(name = "seats_per_row")
    @Max(30)
    private int seatsPerRow;

    @Column
    @Max(30)
    private int rows;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SectorCategory getCategory() {
        return category;
    }

    public void setCategory(SectorCategory category) {
        this.category = category;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sector)) return false;
        Sector sector = (Sector) o;
        return getStartPositionX() == sector.getStartPositionX() &&
            getStartPositionY() == sector.getStartPositionY() &&
            getSeatsPerRow() == sector.getSeatsPerRow() &&
            getRows() == sector.getRows() &&
            Objects.equals(getId(), sector.getId()) &&
            Objects.equals(getCategory(), sector.getCategory());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getCategory(), getStartPositionX(), getStartPositionY(), getSeatsPerRow(), getRows());
    }

    @Override
    public String toString() {
        return "Sector{" +
            "id=" + id +
            ", category=" + category +
            ", startPositionX=" + startPositionX +
            ", startPositionY=" + startPositionY +
            ", seatsPerRow=" + seatsPerRow +
            ", rows=" + rows +
            '}';
    }


}
