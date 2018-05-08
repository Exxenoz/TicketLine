package at.ac.tuwien.inso.sepm.ticketline.rest.reservation;

import io.swagger.annotations.ApiModel;

import java.util.Objects;

@ApiModel(value = "ReservationFilterTopTenDTO", description = "The reservation filter top ten DTO for filtered reservation entries via rest")
public class ReservationFilterTopTenDTO {

    private Integer month;
    private Long eventId;

    public ReservationFilterTopTenDTO() {
    }

    public ReservationFilterTopTenDTO(Integer month, Long eventId) {
        this.month = month;
        this.eventId = eventId;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @Override
    public String toString() {
        return "ReservationFilterTopTenDTO{" +
            "month=" + month +
            ", eventId=" + eventId +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReservationFilterTopTenDTO that = (ReservationFilterTopTenDTO) o;

        return Objects.equals(month, that.month) &&
            Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(month, eventId);
    }
}
