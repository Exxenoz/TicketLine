package at.ac.tuwien.inso.sepm.ticketline.rest.event;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(value = "EventResponseTopTenDTO", description = "The event response DTO for top ten event entries via rest")
public class EventResponseTopTenDTO {

    @ApiModelProperty(required = true, readOnly = true, name = "One event of the top 10 event entries")
    private EventDTO event;

    @ApiModelProperty(required = true, readOnly = true, name = "The paid reservations of the event")
    private long sales;

    public EventResponseTopTenDTO() {
    }

    public EventResponseTopTenDTO(EventDTO event, long sales) {
        this.event = event;
        this.sales = sales;
    }

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public long getSales() {
        return sales;
    }

    public void setSales(long sales) {
        this.sales = sales;
    }

    @Override
    public String toString() {
        return "EventResponseTopTenDTO{" +
            "event=" + event +
            ", sales=" + sales +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventResponseTopTenDTO that = (EventResponseTopTenDTO) o;
        return sales == that.sales &&
            Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {

        return Objects.hash(event, sales);
    }
}
