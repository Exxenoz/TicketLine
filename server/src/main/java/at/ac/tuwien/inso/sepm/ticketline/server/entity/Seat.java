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
    public String toString() {
        return "Seat{" +
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

        Seat seat = (Seat) o;

        return Objects.equals(id, seat.id) &&
            Objects.equals(positionX, seat.positionX) &&
            Objects.equals(positionY, seat.positionY) &&
            Objects.equals(sector, seat.sector);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, positionX, positionY, sector);
    }
}
