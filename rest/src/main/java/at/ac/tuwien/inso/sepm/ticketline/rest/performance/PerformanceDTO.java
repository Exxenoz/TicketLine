package at.ac.tuwien.inso.sepm.ticketline.rest.performance;

import at.ac.tuwien.inso.sepm.ticketline.rest.Hall.HallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.address.LocationAddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PerformanceDTO {

    private long id;
    private EventDTO event;
    private Set<ArtistDTO> artists = new HashSet<>();
    private String name;

    /**
     * The base price of the performance, mapped in cents
     */
    private Long price;
    @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime performanceStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime performanceEnd;

    private LocationAddressDTO address;

    private HallDTO hall;

    public PerformanceDTO() {
    }

    public PerformanceDTO(EventDTO event, Set<ArtistDTO> artists, String name, Long price, LocalDateTime performanceStart, LocalDateTime performanceEnd, LocationAddressDTO address) {
        this.event = event;
        this.artists = artists;
        this.name = name;
        this.price = price;
        this.performanceStart = performanceStart;
        this.performanceEnd = performanceEnd;
        this.address = address;
    }

    public Set<ArtistDTO> getArtists() {
        return artists;
    }

    public void setArtists(Set<ArtistDTO> artists) {
        this.artists = artists;
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

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
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

    public LocationAddressDTO getLocationAddress() {
        return address;
    }

    public void setLocationAddress(LocationAddressDTO address) {
        this.address = address;
    }

    public LocationAddressDTO getAddress() {
        return address;
    }

    public HallDTO getHall() {
        return hall;
    }

    public void setHall(HallDTO hall) {
        this.hall = hall;
    }

    @Override
    public String toString() {
        return "PerformanceDTO{" +
            "id=" + id +
            ", event=" + event +
            ", artists=" + artists +
            ", name='" + name + '\'' +
            ", price=" + price +
            ", performanceStart=" + performanceStart +
            ", performanceEnd=" + performanceEnd +
            ", address=" + address +
            ", hall=" + hall +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerformanceDTO that = (PerformanceDTO) o;
        return id == that.id &&
            Objects.equals(event, that.event) &&
            Objects.equals(artists, that.artists) &&
            Objects.equals(name, that.name) &&
            Objects.equals(price, that.price) &&
            Objects.equals(performanceStart, that.performanceStart) &&
            Objects.equals(performanceEnd, that.performanceEnd) &&
            Objects.equals(address, that.address) &&
            Objects.equals(hall, that.hall);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, event, artists, name, price, performanceStart, performanceEnd, address, hall);
    }
}
