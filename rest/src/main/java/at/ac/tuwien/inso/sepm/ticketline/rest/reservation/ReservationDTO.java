package at.ac.tuwien.inso.sepm.ticketline.rest.reservation;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
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

    @ApiModelProperty(required = true, readOnly = true, name = "The date and time when the reservation was paid")
    private String reservationNumber;

    @ApiModelProperty(required = true, readOnly = true, name = "The cancelation state of the reservation")
    private boolean canceled;

    @ApiModelProperty(required = false, readOnly = true, name = "Maps the price of a reservatition for cancellation purposed." +
        "Do not use it for important calculations")
    private Long elusivePrice;

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

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public Long getElusivePrice() {
        return elusivePrice;
    }

    public void setElusivePrice(Long elusivePrice) {
        this.elusivePrice = elusivePrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationDTO)) return false;
        ReservationDTO that = (ReservationDTO) o;
        return isCanceled() == that.isCanceled() &&
            Objects.equals(getId(), that.getId()) &&
            Objects.equals(getCustomer(), that.getCustomer()) &&
            Objects.equals(getPerformance(), that.getPerformance()) &&
            Objects.equals(getSeats(), that.getSeats()) &&
            Objects.equals(isPaid, that.isPaid) &&
            Objects.equals(getPaidAt(), that.getPaidAt()) &&
            Objects.equals(getReservationNumber(), that.getReservationNumber()) &&
            Objects.equals(getElusivePrice(), that.getElusivePrice());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getCustomer(), getPerformance(), getSeats(), isPaid, getPaidAt(), getReservationNumber(), isCanceled(), getElusivePrice());
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
            "id=" + id +
            ", customer=" + customer +
            ", performance=" + performance +
            ", seats=" + seats +
            ", isPaid=" + isPaid +
            ", paidAt=" + paidAt +
            ", reservationNumber='" + reservationNumber + '\'' +
            ", canceled=" + canceled +
            ", elusivePrice=" + elusivePrice +
            '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public static final class Builder {
        private Long id;
        private CustomerDTO customer;
        private PerformanceDTO performance;
        private List<SeatDTO> seats;
        private Boolean isPaid;
        private LocalDateTime paidAt;
        private Long elusivePrice;


        private Builder() {
        }

        public static Builder aReservationDTO() {
            return new Builder();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withCustomer(CustomerDTO customer) {
            this.customer = customer;
            return this;
        }

        public Builder withPerformance(PerformanceDTO performance) {
            this.performance = performance;
            return this;
        }

        public Builder withSeats(List<SeatDTO> seats) {
            this.seats = seats;
            return this;
        }

        public Builder withIsPaid(Boolean isPaid) {
            this.isPaid = isPaid;
            return this;
        }

        public Builder withPaidAt(LocalDateTime paidAt) {
            this.paidAt = paidAt;
            return this;
        }

        public Builder withElusivePrice(Long elusivePrice) {
            this.elusivePrice = elusivePrice;
            return this;
        }

        public ReservationDTO build() {
            ReservationDTO reservationDTO = new ReservationDTO();
            reservationDTO.setId(id);
            reservationDTO.setPerformance(performance);
            reservationDTO.setSeats(seats);
            reservationDTO.setPaidAt(paidAt);
            reservationDTO.customer = this.customer;
            reservationDTO.isPaid = this.isPaid;
            reservationDTO.elusivePrice = this.elusivePrice;
            return reservationDTO;
        }
    }
}
