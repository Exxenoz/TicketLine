package at.ac.tuwien.inso.sepm.ticketline.rest.reservation;

import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import org.springframework.data.domain.Sort;

import java.util.Objects;

public class ReservationSearchDTO extends PageRequestDTO {
    private String firstName;
    private String lastName;
    private String performanceName;

    public ReservationSearchDTO() {
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

    public String getPerformanceName() {
        return performanceName;
    }

    public void setPerformanceName(String performanceName) {
        this.performanceName = performanceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationSearchDTO)) return false;
        if (!super.equals(o)) return false;
        ReservationSearchDTO that = (ReservationSearchDTO) o;
        return Objects.equals(getFirstName(), that.getFirstName()) &&
            Objects.equals(getLastName(), that.getLastName()) &&
            Objects.equals(getPerformanceName(), that.getPerformanceName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getFirstName(), getLastName(), getPerformanceName());
    }

    @Override
    public String toString() {
        return "ReservationSearchDTO{" +
            "firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", performanceName='" + performanceName + '\'' +
            ", " + super.toString() + " }";
    }

    public static final class Builder {
        private String firstName;
        private String lastName;
        private String performanceName;
        private int page;
        private int size;
        private Sort.Direction sortDirection;
        private String sortColumnName;

        private Builder() {
        }

        public static Builder aReservationSearchDTO() {
            return new Builder();
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withPerformanceName(String performanceName) {
            this.performanceName = performanceName;
            return this;
        }

        public Builder withPage(int page) {
            this.page = page;
            return this;
        }

        public Builder withSize(int size) {
            this.size = size;
            return this;
        }

        public Builder withSortDirection(Sort.Direction sortDirection) {
            this.sortDirection = sortDirection;
            return this;
        }

        public Builder withSortColumnName(String sortColumnName) {
            this.sortColumnName = sortColumnName;
            return this;
        }

        public ReservationSearchDTO build() {
            ReservationSearchDTO reservationSearchDTO = new ReservationSearchDTO();
            reservationSearchDTO.setFirstName(firstName);
            reservationSearchDTO.setLastName(lastName);
            reservationSearchDTO.setPerformanceName(performanceName);
            reservationSearchDTO.setPage(page);
            reservationSearchDTO.setSize(size);
            reservationSearchDTO.setSortDirection(sortDirection);
            reservationSearchDTO.setSortColumnName(sortColumnName);
            return reservationSearchDTO;
        }
    }
}
