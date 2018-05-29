package at.ac.tuwien.inso.sepm.ticketline.rest.reservation;

import java.util.List;

public class CreateReservationDTO {

    //TODO: Customer
    private long customerID;

    private long performanceID;
    private List<Long> seatIDs;
    private boolean paid;

    public long getPerformanceID() {
        return performanceID;
    }

    public void setPerformanceID(long performanceID) {
        this.performanceID = performanceID;
    }

    public List<Long> getSeatIDs() {
        return seatIDs;
    }

    public void setSeatIDs(List<Long> seatIDs) {
        this.seatIDs = seatIDs;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(long customerID) {
        this.customerID = customerID;
    }


    public static final class CreateReservationDTOBuilder {
        //TODO: Customer
        private long customerID;
        private long performanceID;
        private List<Long> seatIDs;
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

        public CreateReservationDTOBuilder withSeatIDs(List<Long> seatIDs) {
            this.seatIDs = seatIDs;
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
            createReservationDTO.setSeatIDs(seatIDs);
            createReservationDTO.setPaid(paid);
            return createReservationDTO;
        }
    }
}
