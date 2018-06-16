package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.util.Objects;

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

    public static final class Builder {
        private Long id;
        private SectorCategory category;
        private int startPositionX;
        private int startPositionY;
        private int seatsPerRow;
        private int rows;

        private Builder() {
        }

        public static Builder aSector() {
            return new Builder();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withCategory(SectorCategory category) {
            this.category = category;
            return this;
        }

        public Builder withStartPositionX(int startPositionX) {
            this.startPositionX = startPositionX;
            return this;
        }

        public Builder withStartPositionY(int startPositionY) {
            this.startPositionY = startPositionY;
            return this;
        }

        public Builder withSeatsPerRow(int seatsPerRow) {
            this.seatsPerRow = seatsPerRow;
            return this;
        }

        public Builder withRows(int rows) {
            this.rows = rows;
            return this;
        }

        public Sector build() {
            Sector sector = new Sector();
            sector.setId(id);
            sector.setCategory(category);
            sector.setStartPositionX(startPositionX);
            sector.setStartPositionY(startPositionY);
            sector.setSeatsPerRow(seatsPerRow);
            sector.setRows(rows);
            return sector;
        }
    }
}
