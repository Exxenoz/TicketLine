package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;
import java.util.Objects;

@Embeddable
public class LocationAddress extends BaseAddress {

    @Column(nullable = false)
    @Size(max = 100)
    private String locationName;

    public LocationAddress() {
    }

    public LocationAddress(@Size(max = 100) String locationName,
                       @Size(max = 100) String street,
                       @Size(max = 100) String city,
                       @Size(max = 100) String country,
                       String postalCode) {
        super(street, city, country, postalCode);
        this.locationName = locationName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @Override
    public String toString() {
        return "LocationAddressDTO{" +
            ", locationName='" + locationName + '\'' +
            ", address='" + super.toString() +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LocationAddress that = (LocationAddress) o;
        return Objects.equals(locationName, that.locationName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), locationName);
    }
}
