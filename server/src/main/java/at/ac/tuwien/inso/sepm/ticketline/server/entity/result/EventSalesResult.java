package at.ac.tuwien.inso.sepm.ticketline.server.entity.result;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;

import java.util.Objects;

public class EventSalesResult {

    private Event event;
    private long sales;

    public EventSalesResult() {
    }

    public EventSalesResult(Event event, long sales) {
        this.event = event;
        this.sales = sales;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
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
        return "EventSalesResult{" +
            "event=" + event +
            ", sales=" + sales +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventSalesResult that = (EventSalesResult) o;
        return sales == that.sales &&
            Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {

        return Objects.hash(event, sales);
    }
}
