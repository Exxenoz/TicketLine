package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;

import java.io.File;

public interface InvoiceRestClient {

    void downloadPDF(String reservationNumber) throws DataAccessException;
}
