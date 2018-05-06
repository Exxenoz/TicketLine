package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.util.Objects;

@Embeddable
public class Address {

    @Column(nullable = false)
    @Size(max = 100)
    private String locationName;

    @Column(nullable = false)
    @Size(max = 100)
    private String street;

    @Column(nullable = false)
    @Size(max = 100)
    private String city;

    @Column(nullable = false)
    @Size(max = 100)
    private String country;

    @Column(nullable = false)
    private String postalCode;

    public Address() {
    }

    public Address(@Size(max = 100) String locationName,
                   @Size(max = 100) String street,
                   @Size(max = 100) String city,
                   @Size(max = 100) String country,
                   String postalCode) {
        this.locationName = locationName;
        this.street = street;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return "Address{" +
            "locationName='" + locationName + '\'' +
            ", street='" + street + '\'' +
            ", city='" + city + '\'' +
            ", country='" + country + '\'' +
            ", postalCode=" + postalCode +
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
        Address address = (Address) o;
        return postalCode.equals(address.postalCode) &&
            Objects.equals(locationName, address.locationName) &&
            Objects.equals(street, address.street) &&
            Objects.equals(city, address.city) &&
            Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationName, street, city, country, postalCode);
    }
}
