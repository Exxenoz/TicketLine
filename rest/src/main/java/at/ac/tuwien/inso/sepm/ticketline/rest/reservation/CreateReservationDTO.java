package at.ac.tuwien.inso.sepm.ticketline.rest.reservation;

import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;

import java.util.List;

public class CreateReservationDTO {

    private Long customerID;
    private long performanceID;
    private List<SeatDTO> seats;
    private boolean paid;

    public long getPerformanceID() {
        return performanceID;
    }

    public void setPerformanceID(long performanceID) {
        this.performanceID = performanceID;
    }

    public List<SeatDTO> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatDTO> seats) {
        this.seats = seats;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }


    public static final class CreateReservationDTOBuilder {

        private Long customerID;
        private long performanceID;
        private List<SeatDTO> seats;
        private boolean paid;

        private CreateReservationDTOBuilder() {
        }

        public static CreateReservationDTOBuilder aCreateReservationDTO() {
            return new CreateReservationDTOBuilder();
        }

        public CreateReservationDTOBuilder withCustomerID(long customerID) {
            this.customerID = customerID;
            return this;
        }

        public CreateReservationDTOBuilder withPerformanceID(long performanceID) {
            this.performanceID = performanceID;
            return this;
        }

        public CreateReservationDTOBuilder withSeats(List<SeatDTO> seatDTOS) {
            this.seats = seatDTOS;
            return this;
        }

        public CreateReservationDTOBuilder withPaid(boolean paid) {
            this.paid = paid;
            return this;
        }

        public CreateReservationDTO build() {
            CreateReservationDTO createReservationDTO = new CreateReservationDTO();
            createReservationDTO.setCustomerID(customerID);
            createReservationDTO.setPerformanceID(performanceID);
            createReservationDTO.setSeats(seats);
            createReservationDTO.setPaid(paid);
            return createReservationDTO;
        }
    }
}
