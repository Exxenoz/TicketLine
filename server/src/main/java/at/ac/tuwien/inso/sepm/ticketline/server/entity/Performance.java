package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class Performance {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "seq_performance_id")
    @SequenceGenerator(name = "seq_performance_id", sequenceName = "seq_performance_id")
    private Long id;

    @ManyToOne
    private Event event;

    @Column(nullable = false)
    @Size(max = 100)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private LocalDateTime performanceStart;

    @Column(nullable = false)
    private LocalDateTime performanceEnd;

    @Column(nullable = false)
    private Address address;

    public Performance() {
    }

    public Performance(Event event, @Size(max = 100) String name, Double price, LocalDateTime performanceStart, LocalDateTime performanceEnd, Address address) {
        this.event = event;
        this.name = name;
        this.price = price;
        this.performanceStart = performanceStart;
        this.performanceEnd = performanceEnd;
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
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

    @Override
    public String toString() {
        return "Performance{" +
            "event=" + event +
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
        Performance that = (Performance) o;
        return Objects.equals(id, that.id) &&
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
