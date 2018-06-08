package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;

import java.util.List;

public interface NewsService {

    /**
     * Find all news entries.
     *
     * @return list of news entries
     * @throws DataAccessException in case something went wrong
     */
    List<SimpleNewsDTO> findAll() throws DataAccessException;

    /**
     * Retrieve detailed information for a specific news entry
     * @param id if of news to search by
     * @return detailed news
     * @throws DataAccessException in case something went wrong
     */
    DetailedNewsDTO find(Long id) throws DataAccessException;

    /**
     * Publish new news
     *
     * @param detailedNewsDTO news dto containing title, image and article
     * @return published news
     * @throws DataAccessException in case something went wrong
     */
    SimpleNewsDTO publish(DetailedNewsDTO detailedNewsDTO) throws DataAccessException;
}
