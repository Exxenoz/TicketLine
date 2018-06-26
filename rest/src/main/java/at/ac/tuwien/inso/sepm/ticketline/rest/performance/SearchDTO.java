package at.ac.tuwien.inso.sepm.ticketline.rest.performance;

import at.ac.tuwien.inso.sepm.ticketline.rest.address.BaseAddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.address.LocationAddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SearchDTO extends PageResponseDTO {
    private String performanceName;
    private String eventName;
    private String firstName;
    private String lastName;
    private EventTypeDTO eventType;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime performanceStart;
    private Long price;
    private String locationName;
    private String street;
    private String city;
    private String country;
    private String postalCode;
    private Duration duration;

    public SearchDTO() {
    }

    public SearchDTO(String performanceName, String eventName, String firstName,
                     String lastName, EventTypeDTO eventType, LocalDateTime performanceStart,
                     Long price, String locationName, String street, String city, String country,
                     String postalCode, Duration duration) {
        this.performanceName = performanceName;
        this.eventName = eventName;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getPerformanceStart() {
        return performanceStart;
    }

    public void setPerformanceStart(LocalDateTime performanceStart) {
        this.performanceStart = performanceStart;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        SearchDTO searchDTO = (SearchDTO) o;
        return Objects.equals(performanceName, searchDTO.performanceName) &&
            Objects.equals(eventName, searchDTO.eventName) &&
            Objects.equals(firstName, searchDTO.firstName) &&
            Objects.equals(lastName, searchDTO.lastName) &&
            eventType == searchDTO.eventType &&
            Objects.equals(performanceStart, searchDTO.performanceStart) &&
            Objects.equals(price, searchDTO.price) &&
            Objects.equals(locationName, searchDTO.locationName) &&
            Objects.equals(street, searchDTO.street) &&
            Objects.equals(city, searchDTO.city) &&
            Objects.equals(country, searchDTO.country) &&
            Objects.equals(postalCode, searchDTO.postalCode) &&
            Objects.equals(duration, searchDTO.duration);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(),
            performanceName, eventName, firstName,
            lastName, eventType, performanceStart,
            price, locationName, street,
            city, country, postalCode, duration);
    }

    public LocationAddressDTO getLocationAddressDTO(){
        return new LocationAddressDTO(locationName, street, city, country, postalCode);
    }

    public BaseAddressDTO getBaseAddressDTO(){
        return new BaseAddressDTO(street, city, country, postalCode);
    }
}
