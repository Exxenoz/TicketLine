package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.info.Info;

public interface InfoRestClient {

    /**
     * Load info object
     *
     * @return info object representing information from the ticketline server
     * @throws DataAccessException in case something went wrong
     */
    Info find() throws DataAccessException;

}
