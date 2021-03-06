package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.NewsRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SimpleNewsService implements NewsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final NewsRestClient newsRestClient;

    public SimpleNewsService(NewsRestClient newsRestClient) {
        this.newsRestClient = newsRestClient;
    }

    @Override
    public PageResponseDTO<SimpleNewsDTO> findAllUnread(PageRequestDTO request) throws DataAccessException {
        return newsRestClient.findAllUnread(request);
    }

    @Override
    public PageResponseDTO<SimpleNewsDTO> findAllRead(PageRequestDTO request) throws DataAccessException {
        return newsRestClient.findAllRead(request);
    }

    @Override
    public DetailedNewsDTO find(Long id) throws DataAccessException {
        return newsRestClient.find(id);
    }

    @Override
    public SimpleNewsDTO publish(DetailedNewsDTO detailedNewsDTO) throws DataAccessException {
        return newsRestClient.publish(detailedNewsDTO);
    }
}
