package at.ac.tuwien.inso.sepm.ticketline.rest.address;

import java.util.Objects;

public class LocationAddressDTO extends BaseAddressDTO{

    private String locationName;

    public LocationAddressDTO() {
    }

    public LocationAddressDTO(String locationName, String street, String city, String country, String postalCode) {
        super(street, city, country, postalCode);
        this.locationName = locationName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void update(LocationAddressDTO locationAddressDTO) {
        locationName = locationAddressDTO.locationName;
        super.update(locationAddressDTO);
    }

    @Override
    public String toString() {
        return "LocationAddressDTO{" +
            "locationName='" + locationName + '\'' +
            ", address='" + super.toString() +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LocationAddressDTO that = (LocationAddressDTO) o;
        return Objects.equals(locationName, that.locationName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), locationName);
    }
}
