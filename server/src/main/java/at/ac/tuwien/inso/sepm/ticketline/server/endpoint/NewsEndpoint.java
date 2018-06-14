package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.news.NewsMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.endpoint.HttpBadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.endpoint.HttpForbiddenException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.endpoint.HttpNotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalBadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalForbiddenException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalNewsValidationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalNotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/news")
@Api(value = "news")
public class NewsEndpoint {

    private final NewsService newsService;
    private final NewsMapper newsMapper;

    public NewsEndpoint(NewsService newsService, NewsMapper newsMapper) {
        this.newsService = newsService;
        this.newsMapper = newsMapper;
    }

    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Get list of simple news entries")
    public PageResponseDTO<SimpleNewsDTO> findAll(@RequestParam("read") Boolean read, PageRequestDTO pageRequestDTO) {
        try {
            return read ? newsService.findAllRead(pageRequestDTO) :
                newsService.findAllUnread(pageRequestDTO);
        } catch (InternalForbiddenException e) {
            throw new HttpForbiddenException();
        } catch (InternalBadRequestException e) {
            throw new HttpBadRequestException();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Get detailed information about a specific news entry")
    public DetailedNewsDTO find(@PathVariable Long id) {
        try {
            return newsMapper.newsToDetailedNewsDTO(newsService.findOne(id));
        } catch (InternalNotFoundException e) {
            throw new HttpNotFoundException();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Publish a new news entry")
    public SimpleNewsDTO publishNews(@RequestBody DetailedNewsDTO detailedNewsDTO) {
        try {
            return newsService.publishNews(detailedNewsDTO);
        } catch (InternalNewsValidationException e) {
            throw new HttpBadRequestException();
        }
    }
}
