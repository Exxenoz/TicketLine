package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "seq_reservation_id")
    @SequenceGenerator(name = "seq_reservation_id", sequenceName = "seq_reservation_id")
    private Long id;

    // ToDo: Add customer
    @ManyToOne(cascade = CascadeType.DETACH)
    private Customer customer;

    @ManyToOne
    private Performance performance;

    @ManyToMany
    private List<Seat> seats;

    @Column(nullable = false)
    private Boolean paid;

    @Column(nullable = true, name = "paid_at")
    private LocalDateTime paidAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public Boolean isPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    @Override
    public String toString() {
        return "Reservation{" +
            "id=" + id +
            ", performance=" + performance +
            ", seats=" + seats +
            ", paid=" + paid +
            ", paidAt=" + paidAt +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reservation that = (Reservation) o;

        return Objects.equals(id, that.id) &&
            Objects.equals(performance, that.performance) &&
            Objects.equals(seats, that.seats) &&
            Objects.equals(paid, that.paid) &&
            Objects.equals(paidAt, that.paidAt);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, performance, seats, paid, paidAt);
    }
}
