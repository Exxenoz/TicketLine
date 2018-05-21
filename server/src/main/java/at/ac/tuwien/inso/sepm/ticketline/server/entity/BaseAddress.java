package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;
import java.util.Objects;

@Embeddable
@MappedSuperclass
public class BaseAddress {

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

    public BaseAddress() {
    }

    public BaseAddress(@Size(max = 100) String street,
                       @Size(max = 100) String city,
                       @Size(max = 100) String country,
                       String postalCode) {
        this.street = street;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
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
        return "BaseAddress{" +
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
        BaseAddress baseAddress = (BaseAddress) o;
        return postalCode.equals(baseAddress.postalCode) &&
            Objects.equals(street, baseAddress.street) &&
            Objects.equals(city, baseAddress.city) &&
            Objects.equals(country, baseAddress.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, country, postalCode);
    }
}
