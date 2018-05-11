package at.ac.tuwien.inso.sepm.ticketline.rest.performance;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

public class SearchDTO {
    private String performanceName;
    private String eventName;
    private String artistFirstName;
    private String artistLastName;
    private EventTypeDTO eventType;
    @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime performanceStart;
    private BigDecimal price;

    private String locationName;
    private String street;
    private String city;
    private String country;
    private String postalCode;
    private Duration duration;

    public SearchDTO() {
    }

    public SearchDTO(String performanceName, String eventName, String artistFirstName,
                     String artistLastName, EventTypeDTO eventType, LocalDateTime performanceStart,
                     BigDecimal price, String locationName, String street, String city, String country,
                     String postalCode, Duration duration) {
        this.performanceName = performanceName;
        this.eventName = eventName;
        this.artistFirstName = artistFirstName;
        this.artistLastName = artistLastName;
        this.eventType = eventType;
        this.performanceStart = performanceStart;
        this.price = price;
        this.locationName = locationName;
        this.street = street;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.duration = duration;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public EventTypeDTO getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeDTO eventType) {
        this.eventType = eventType;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getArtistFirstName() {
        return artistFirstName;
    }

    public void setArtistFirstName(String artistFirstName) {
        this.artistFirstName = artistFirstName;
    }

    public String getArtistLastName() {
        return artistLastName;
    }

    public void setArtistLastName(String artistLastName) {
        this.artistLastName = artistLastName;
    }

    public LocalDateTime getPerformanceStart() {
        return performanceStart;
    }

    public void setPerformanceStart(LocalDateTime performanceStart) {
        this.performanceStart = performanceStart;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPerformanceName() {
        return performanceName;
    }

    public void setPerformanceName(String performanceName) {
        this.performanceName = performanceName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}