package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import java.time.LocalDate;
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


    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Performance performance;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Seat> seats;

    @Column(nullable = false)
    private Boolean paid;

    @Column(nullable = true, name = "paid_at")
    private LocalDateTime paidAt;

    @Column(nullable = false, unique = true)
    private String reservationNumber;

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

    public Boolean getPaid() {
        return paid;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(customer, that.customer) &&
            Objects.equals(performance, that.performance) &&
            Objects.equals(seats, that.seats) &&
            Objects.equals(paid, that.paid) &&
            Objects.equals(paidAt, that.paidAt) &&
            Objects.equals(reservationNumber, that.reservationNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, customer, performance, seats, paid, paidAt, reservationNumber);
    }

    @Override
    public String toString() {
        return "Reservation{" +
            "id=" + id +
            ", customer=" + customer +
            ", performance=" + performance +
            ", seats=" + seats +
            ", paid=" + paid +
            ", paidAt=" + paidAt +
            ", reservationNumber='" + reservationNumber + '\'' +
            '}';
    }

    public static final class Builder {
        private Long id;
        private Customer customer;
        private Performance performance;
        private List<Seat> seats;
        private Boolean paid;
        private LocalDateTime paidAt;
        private String reservationNumber;

        private Builder() {
        }

        public static Builder aReservation() {
            return new Builder();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withCustomer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder withPerformance(Performance performance) {
            this.performance = performance;
            return this;
        }

        public Builder withSeats(List<Seat> seats) {
            this.seats = seats;
            return this;
        }

        public Builder withPaid(Boolean paid) {
            this.paid = paid;
            return this;
        }

        public Builder withPaidAt(LocalDateTime paidAt) {
            this.paidAt = paidAt;
            return this;
        }

        public Builder withReservationNumber(String reservationNumber) {
            this.reservationNumber = reservationNumber;
            return this;
        }

        public Reservation build() {
            Reservation reservation = new Reservation();
            reservation.setId(id);
            reservation.setCustomer(customer);
            reservation.setPerformance(performance);
            reservation.setSeats(seats);
            reservation.setPaid(paid);
            reservation.setPaidAt(paidAt);
            reservation.setReservationNumber(reservationNumber);
            return reservation;
        }
    }
}
