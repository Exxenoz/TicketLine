package at.ac.tuwien.inso.sepm.ticketline.rest.performance;

import at.ac.tuwien.inso.sepm.ticketline.rest.address.AddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

public class PerformanceDTO {

    private long id;
    private EventDTO event;
    private String name;
    private Double price;
    @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime performanceStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime performanceEnd;
    private AddressDTO address;

    public PerformanceDTO() {
    }

    public PerformanceDTO(EventDTO event, String name, Double price, LocalDateTime performanceStart, LocalDateTime performanceEnd, AddressDTO address) {
        this.event = event;
        this.name = name;
        this.price = price;
        this.performanceStart = performanceStart;
        this.performanceEnd = performanceEnd;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getPerformanceStart() {
        return performanceStart;
    }

    public void setPerformanceStart(LocalDateTime performanceStart) {
        this.performanceStart = performanceStart;
    }

    public LocalDateTime getPerformanceEnd() {
        return performanceEnd;
    }

    public void setPerformanceEnd(LocalDateTime performanceEnd) {
        this.performanceEnd = performanceEnd;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "PerformanceDTO{" +
            ", event=" + event +
            ", name='" + name + '\'' +
            ", price=" + price +
            ", performanceStart=" + performanceStart +
            ", performanceEnd=" + performanceEnd +
            ", address=" + address +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PerformanceDTO that = (PerformanceDTO) o;
        return id == that.id &&
            Objects.equals(event, that.event) &&
            Objects.equals(name, that.name) &&
            Objects.equals(price, that.price) &&
            Objects.equals(performanceStart, that.performanceStart) &&
            Objects.equals(performanceEnd, that.performanceEnd) &&
            Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, event, name, price, performanceStart, performanceEnd, address);
    }
}
