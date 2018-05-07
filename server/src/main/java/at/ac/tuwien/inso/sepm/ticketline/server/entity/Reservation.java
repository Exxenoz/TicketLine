package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import com.github.javafaker.Bool;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "seq_reservation_id")
    @SequenceGenerator(name = "seq_reservation_id", sequenceName = "seq_reservation_id")
    private Long id;

    // ToDo: Add customer
    //@ManyToOne
    //private Customer customer;

    @ManyToOne
    private Performance performance;

    @ManyToOne
    private Seat seat;

    @Column(nullable = false)
    private Boolean isPaid;

    @Column(nullable = false, name = "paid_at")
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

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public Boolean isPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
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
            ", seat=" + seat +
            ", isPaid=" + isPaid +
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
            Objects.equals(seat, that.seat) &&
            Objects.equals(isPaid, that.isPaid) &&
            Objects.equals(paidAt, that.paidAt);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, performance, seat, isPaid, paidAt);
    }
}
