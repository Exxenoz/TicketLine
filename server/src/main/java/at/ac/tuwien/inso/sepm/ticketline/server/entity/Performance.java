package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
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

    @ManyToMany
    private Set<Artist> artists = new HashSet<>();

    @Column(nullable = false)
    @Size(max = 100)
    @NotNull
    private String name;

    @Column(nullable = false)
    @NotNull
    private BigDecimal price;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime performanceStart;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime performanceEnd;

    @Column(nullable = false)
    @NotNull
    private Address address;

    public Performance() {
    }

    public Performance(Event event, Set<Artist> artists, @Size(max = 100) String name, BigDecimal price, LocalDateTime performanceStart, LocalDateTime performanceEnd, Address address) {
        this.event = event;
        this.artists = artists;
        this.name = name;
        this.price = price;
        this.performanceStart = performanceStart;
        this.performanceEnd = performanceEnd;
        this.address = address;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
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
            "name= " + name +
            ", price= " + price +
            ", performanceStart= " + performanceStart +
            ", performanceEnd= " + performanceEnd +
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
            Objects.equals(name, that.name) &&
            Objects.equals(price, that.price) &&
            Objects.equals(performanceStart, that.performanceStart) &&
            Objects.equals(performanceEnd, that.performanceEnd);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, event, name, price, performanceStart, performanceEnd);
    }
}
