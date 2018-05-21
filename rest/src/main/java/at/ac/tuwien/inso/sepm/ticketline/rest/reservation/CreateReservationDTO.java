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
}
