package at.ac.tuwien.inso.sepm.ticketline.rest.reservation;

import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(value = "ReservationDTO", description = "The reservation DTO for reservation entries via rest")
public class ReservationDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    // ToDo: Add customer DTO
    //@ApiModelProperty(required = true, readOnly = true, name = "The customer of the reservation")
    //private CustomerDTO customer;

    @ApiModelProperty(required = true, readOnly = true, name = "The performance of the reservation")
    private PerformanceDTO performance;

    @ApiModelProperty(required = true, readOnly = true, name = "The seat of the reservation")
    private SeatDTO seat;

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

    public SeatDTO getSeat() {
        return seat;
    }

    public void setSeat(SeatDTO seat) {
        this.seat = seat;
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
            "id=" + id +
            ", performance=" + performance +
            ", seat=" + seat +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReservationDTO that = (ReservationDTO) o;

        return Objects.equals(id, that.id) &&
            Objects.equals(performance, that.performance) &&
            Objects.equals(seat, that.seat);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, performance, seat);
    }
}
