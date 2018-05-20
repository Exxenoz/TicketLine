package at.ac.tuwien.inso.sepm.ticketline.rest.address;

import java.util.Objects;

public class AddressDTO {

    private String locationName;
    private String street;
    private String city;
    private String country;
    private String postalCode;

    public AddressDTO() {
    }

    public AddressDTO(String locationName, String street, String city, String country, String postalCode) {
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

    public void update(AddressDTO addressDTO) {
        locationName = addressDTO.locationName;
        street = addressDTO.street;
        postalCode = addressDTO.postalCode;
        city = addressDTO.city;
        country = addressDTO.country;
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
            ", locationName='" + locationName + '\'' +
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
        AddressDTO that = (AddressDTO) o;
        return postalCode.equals(that.postalCode) &&
            Objects.equals(locationName, that.locationName) &&
            Objects.equals(street, that.street) &&
            Objects.equals(city, that.city) &&
            Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {

        return Objects.hash(locationName, street, city, country, postalCode);
    }
}
