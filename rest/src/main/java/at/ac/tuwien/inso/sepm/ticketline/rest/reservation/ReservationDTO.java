package at.ac.tuwien.inso.sepm.ticketline.rest.reservation;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@ApiModel(value = "ReservationDTO", description = "The reservation DTO for reservation entries via rest")
public class ReservationDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The customer of the reservation")
    private CustomerDTO customer;

    @ApiModelProperty(required = true, readOnly = true, name = "The performance of the reservation")
    private PerformanceDTO performance;

    @ApiModelProperty(required = true, readOnly = true, name = "The seat of the reservation")
    private List<SeatDTO> seats;

    @ApiModelProperty(required = true, readOnly = true, name = "The pay state of the reservation")
    private Boolean isPaid;

    @ApiModelProperty(required = true, readOnly = true, name = "The date and time when the reservation was paid")
    private LocalDateTime paidAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PerformanceDTO getPerformance() {
        return performance;
    }

    public void setPerformance(PerformanceDTO performance) {
        this.performance = performance;
    }

    public List<SeatDTO> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatDTO> seats) {
        this.seats = seats;
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
        return "ReservationDTO{" +
            "id=" + id +
            ", performance=" + performance +
            ", seat=" + seats +
            ", isPaid=" + isPaid +
            ", paidAt=" + paidAt +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReservationDTO that = (ReservationDTO) o;

        return Objects.equals(id, that.id) &&
            Objects.equals(performance, that.performance) &&
            Objects.equals(seats, that.seats) &&
            Objects.equals(isPaid, that.isPaid) &&
            Objects.equals(paidAt, that.paidAt);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, performance, seats, isPaid, paidAt);
    }
}
