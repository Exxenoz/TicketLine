package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import java.util.Objects;

public class ReservationFilterTopTen {

    private Integer month;

    private Long eventId;

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
        return "ReservationFilterTopTen{" +
            "month=" + month +
            ", eventId=" + eventId +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReservationFilterTopTen that = (ReservationFilterTopTen) o;

        return Objects.equals(month, that.month) &&
            Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(month, eventId);
    }
}
