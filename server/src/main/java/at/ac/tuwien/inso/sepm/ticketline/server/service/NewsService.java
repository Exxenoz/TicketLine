package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.*;

import java.util.List;

public interface NewsService {

    /**
     * Find all news entries ordered by published at date (descending).
     *
     * @return ordered list of all news entries
     */
    List<News> findAll();

    /**
     * Find all unread news entries for the authenticated user ordered by published at date (descending).
     *
     * @param pageRequestDTO information about the page request
     * @return ordered list of all unread news entries for the given username
     * @throws InternalForbiddenException in case the authentication failed
     * @throws InternalBadRequestException in case page or size are invalid
     */
    PageResponseDTO<SimpleNewsDTO> findAllUnread(PageRequestDTO pageRequestDTO) throws InternalForbiddenException, InternalBadRequestException;

    /**
     * Find all read news entries for the authenticated user ordered by published at date (descending).
     *
     * @param pageRequestDTO information about the page request
     * @return ordered list of all read news entries for the given username
     * @throws InternalForbiddenException in case the authentication failed
     * @throws InternalBadRequestException in case page or size are invalid
     */
    PageResponseDTO<SimpleNewsDTO> findAllRead(PageRequestDTO pageRequestDTO) throws InternalForbiddenException, InternalBadRequestException;

    /**
     * Find a single news entry by id.
     *
     * @param id the id of the news entry
     * @throws InternalNotFoundException in case no news entry could be found
     * @return the detailed news entry
     */
    DetailedNewsDTO findOne(Long id) throws InternalNotFoundException;

    /**
     * Publish a single news entry
     *
     * @param detailedNewsDTO to publish
     * @throws InternalNewsValidationException in case the news validation failed
     * @return published news entry
     */
    SimpleNewsDTO publishNews(DetailedNewsDTO detailedNewsDTO) throws InternalNewsValidationException;

    /**
     * Marks a single news entry for the authenticated user as read.
     *
     * @param id the id of the news entry
     * @throws InternalNotFoundException in case no news entry could be found
     * @throws InternalForbiddenException in case the authentication failed
     * @throws InternalUserNotFoundException in case the user could not be found
     */
    void markAsRead(Long id) throws InternalNotFoundException, InternalForbiddenException, InternalUserNotFoundException;
}
