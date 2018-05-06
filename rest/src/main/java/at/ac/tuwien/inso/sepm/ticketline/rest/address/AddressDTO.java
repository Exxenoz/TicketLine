package at.ac.tuwien.inso.sepm.ticketline.rest.address;

import java.util.Objects;

public class AddressDTO {

    private String buildingName;
    private String street;
    private String location;
    private String country;
    private String postalCode;

    public AddressDTO() {
    }

    public AddressDTO(String buildingName, String street, String location, String country, String postalCode) {
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
            ", buildingName='" + buildingName + '\'' +
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
        AddressDTO that = (AddressDTO) o;
        return postalCode.equals(that.postalCode) &&
            Objects.equals(buildingName, that.buildingName) &&
            Objects.equals(street, that.street) &&
            Objects.equals(location, that.location) &&
            Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {

        return Objects.hash(buildingName, street, location, country, postalCode);
    }
}
