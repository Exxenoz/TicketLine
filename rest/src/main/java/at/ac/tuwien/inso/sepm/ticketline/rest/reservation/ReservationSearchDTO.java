package at.ac.tuwien.inso.sepm.ticketline.rest.reservation;

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
