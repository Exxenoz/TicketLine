package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalBadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalForbiddenException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalNewsValidationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalNotFoundException;

import java.util.List;

public interface NewsService {

    /**
     * Find all news entries ordered by published at date (descending).
     *
     * @return ordered list of al news entries
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
     * @param id the is of the news entry
     * @throws InternalNotFoundException in case no news entry could not be found
     * @return the news entry
     */
    News findOne(Long id) throws InternalNotFoundException;

    /**
     * Publish a single news entry
     *
     * @param detailedNewsDTO to publish
     * @throws InternalNewsValidationException in case the news validation failed
     * @return published news entry
     */
    SimpleNewsDTO publishNews(DetailedNewsDTO detailedNewsDTO) throws InternalNewsValidationException;

}
