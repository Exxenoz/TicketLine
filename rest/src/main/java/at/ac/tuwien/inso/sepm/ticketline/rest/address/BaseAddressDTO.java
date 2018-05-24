package at.ac.tuwien.inso.sepm.ticketline.rest.address;

import java.util.Objects;

public class BaseAddressDTO {

    private String street;
    private String city;
    private String country;
    private String postalCode;

    public BaseAddressDTO() {
    }

    public BaseAddressDTO(String street, String city, String country, String postalCode) {
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

    public void update(BaseAddressDTO baseAddressDTO) {
        street = baseAddressDTO.street;
        postalCode = baseAddressDTO.postalCode;
        city = baseAddressDTO.city;
        country = baseAddressDTO.country;
    }

    @Override
    public String toString() {
        return "BaseAddressDTO{" +
            " street= " + street +
            ", city= " + city +
            ", country= " + country +
            ", postalCode= " + postalCode +
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
        BaseAddressDTO that = (BaseAddressDTO) o;
        return postalCode.equals(that.postalCode) &&
            Objects.equals(street, that.street) &&
            Objects.equals(city, that.city) &&
            Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {

        return Objects.hash(street, city, country, postalCode);
    }
}
