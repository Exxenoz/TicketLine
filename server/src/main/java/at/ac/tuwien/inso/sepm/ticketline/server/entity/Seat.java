package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "seq_seat_id")
    @SequenceGenerator(name = "seq_seat_id", sequenceName = "seq_seat_id")
    private Long id;

    @Column(nullable = false)
    private Integer positionX;

    @Column(nullable = false)
    private Integer positionY;

    @ManyToOne
    private Sector sector;

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

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seat)) return false;
        Seat seat = (Seat) o;
        return Objects.equals(getId(), seat.getId()) &&
            Objects.equals(getPositionX(), seat.getPositionX()) &&
            Objects.equals(getPositionY(), seat.getPositionY()) &&
            Objects.equals(getSector(), seat.getSector());
    }

    public boolean equalsWithoutId(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seat)) return false;
        Seat seat = (Seat) o;
        return Objects.equals(getPositionX(), seat.getPositionX()) &&
            Objects.equals(getPositionY(), seat.getPositionY()) &&
            Objects.equals(getSector(), seat.getSector());
    }


    @Override
    public int hashCode() {

        return Objects.hash(getId(), getPositionX(), getPositionY(), getSector());
    }

    @Override
    public String toString() {
        return "seat{" +
            "id=" + id +
            ", positionX=" + positionX +
            ", positionY=" + positionY +
            ", sector=" + sector +
            '}';
    }

    public static final class SeatBuilder {
        private Long id;
        private Integer positionX;
        private Integer positionY;
        private Sector sector;

        private SeatBuilder() {
        }

        public static SeatBuilder aSeat() {
            return new SeatBuilder();
        }

        public SeatBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public SeatBuilder withPositionX(Integer positionX) {
            this.positionX = positionX;
            return this;
        }

        public SeatBuilder withPositionY(Integer positionY) {
            this.positionY = positionY;
            return this;
        }

        public SeatBuilder withSector(Sector sector) {
            this.sector = sector;
            return this;
        }

        public Seat build() {
            Seat seat = new Seat();
            seat.setId(id);
            seat.setPositionX(positionX);
            seat.setPositionY(positionY);
            seat.setSector(sector);
            return seat;
        }
    }
}
