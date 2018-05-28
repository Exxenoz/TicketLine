package at.ac.tuwien.inso.sepm.ticketline.rest.reservation;

import java.util.Objects;

public class ReservationSearchDTO {
    private String firstName;
    private String lastName;
    private String perfomanceName;

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

    public String getPerfomanceName() {
        return perfomanceName;
    }

    public void setPerfomanceName(String perfomanceName) {
        this.perfomanceName = perfomanceName;
    }

    @Override
    public String toString() {
        return "ReservationSearchDTO{" +
            "customerFirstName=" + "\'" + firstName + "\'," +
            "customerLastName=" + "\'" + lastName + "\'," +
            "performanceName=" + "\'" + perfomanceName + "\'" +
            "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ReservationSearchDTO that = (ReservationSearchDTO) obj;
        return (this.firstName.equals(that.getFirstName()))
            && (this.lastName.equals(that.getLastName()))
            && (this.perfomanceName.equals(that.getPerfomanceName()));

    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, perfomanceName);
    }

    public static final class Builder {
        private String firstName;
        private String lastName;
        private String perfomanceName;

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

        public Builder withPerfomanceName(String perfomanceName) {
            this.perfomanceName = perfomanceName;
            return this;
        }

        public ReservationSearchDTO build() {
            ReservationSearchDTO reservationSearchDTO = new ReservationSearchDTO();
            reservationSearchDTO.setFirstName(firstName);
            reservationSearchDTO.setLastName(lastName);
            reservationSearchDTO.setPerfomanceName(perfomanceName);
            return reservationSearchDTO;
        }
    }
}
