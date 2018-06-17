package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;

import java.util.List;

public interface NewsRestClient {

    /**
     * Find a page of news entries, which are already read by the user
     *
     * @param request the object that specifies the request
     * @return page of news entries
     * @throws DataAccessException in case something went wrong
     */
    PageResponseDTO<SimpleNewsDTO> findAllUnread(PageRequestDTO request) throws DataAccessException;

    /**
     * Find a page of news entries, which aren't read by the user yet
     *
     * @param request object specifing the request
     * @return a page of news entries
     * @throws DataAccessException in case something went wrong
     */
    PageResponseDTO<SimpleNewsDTO> findAllRead(PageRequestDTO request) throws DataAccessException;

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
