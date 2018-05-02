package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.util.Objects;

@Embeddable
public class Address {

    @Column(nullable = false)
    @Size(max = 100)
    private String buildingName;

    @Column(nullable = false)
    @Size(max = 100)
    private String street;

    @Column(nullable = false)
    @Size(max = 100)
    private String location;

    @Column(nullable = false)
    @Size(max = 100)
    private String country;

    @Column(nullable = false)
    private int postalCode;

    public Address() {
    }

    public Address(@Size(max = 100) String buildingName,
                   @Size(max = 100) String street,
                   @Size(max = 100) String location,
                   @Size(max = 100) String country,
                   int postalCode) {
        this.buildingName = buildingName;
        this.street = street;
        this.location = location;
        this.country = country;
        this.postalCode = postalCode;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return "Address{" +
            "buildingName='" + buildingName + '\'' +
            ", street='" + street + '\'' +
            ", location='" + location + '\'' +
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
        return postalCode == address.postalCode &&
            Objects.equals(buildingName, address.buildingName) &&
            Objects.equals(street, address.street) &&
            Objects.equals(location, address.location) &&
            Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buildingName, street, location, country, postalCode);
    }
}
