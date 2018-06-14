package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.NewsValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.NewsValidator;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.news.NewsMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalBadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalForbiddenException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalNewsValidationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalNotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.NewsRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.security.IAuthenticationFacade;
import at.ac.tuwien.inso.sepm.ticketline.server.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SimpleNewsService implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    public SimpleNewsService(NewsRepository newsRepository, NewsMapper newsMapper) {
        this.newsRepository = newsRepository;
        this.newsMapper = newsMapper;
    }

    @Override
    public List<News> findAll() {
        return newsRepository.findAllByOrderByPublishedAtDesc();
    }

    @Override
    public PageResponseDTO<SimpleNewsDTO> findAllUnread(PageRequestDTO pageRequestDTO) throws InternalForbiddenException, InternalBadRequestException {
        Authentication authentication = authenticationFacade.getAuthentication();

        if (authentication == null) {
            throw new InternalForbiddenException();
        }

        if (pageRequestDTO == null) {
            throw new InternalBadRequestException();
        }

        if (pageRequestDTO.getPage() < 0) {
            throw new InternalBadRequestException();
        }

        if (pageRequestDTO.getSize() < 1) {
            throw new InternalBadRequestException();
        }

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize(), Sort.by(Sort.Direction.DESC, "publishedAt"));
        Page<News> newsPage = newsRepository.findAllUnreadByUsernameByOrderByPublishedAtDesc(authentication.getName(), pageable);
        return new PageResponseDTO<>(newsMapper.newsToSimpleNewsDTO(newsPage.getContent()), newsPage.getTotalPages());
    }

    @Override
    public PageResponseDTO<SimpleNewsDTO> findAllRead(PageRequestDTO pageRequestDTO) throws InternalForbiddenException, InternalBadRequestException {
        Authentication authentication = authenticationFacade.getAuthentication();

        if (authentication == null) {
            throw new InternalForbiddenException();
        }

        if (pageRequestDTO == null) {
            throw new InternalBadRequestException();
        }

        if (pageRequestDTO.getPage() < 0) {
            throw new InternalBadRequestException();
        }

        if (pageRequestDTO.getSize() < 1) {
            throw new InternalBadRequestException();
        }

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize(), Sort.by(Sort.Direction.DESC, "publishedAt"));
        Page<News> newsPage = newsRepository.findAllReadByUsernameByOrderByPublishedAtDesc(authentication.getName(), pageable);
        return new PageResponseDTO<>(newsMapper.newsToSimpleNewsDTO(newsPage.getContent()), newsPage.getTotalPages());
    }

    @Override
    public News findOne(Long id) throws InternalNotFoundException {
        return newsRepository.findOneById(id).orElseThrow(InternalNotFoundException::new);
    }

    @Override
    public SimpleNewsDTO publishNews(DetailedNewsDTO detailedNewsDTO) throws InternalNewsValidationException {
        try {
            NewsValidator.validateNews(detailedNewsDTO);
        } catch (NewsValidationException e) {
            throw new InternalNewsValidationException();
        }

        News news = newsMapper.detailedNewsDTOToNews(detailedNewsDTO);
        news.setPublishedAt(LocalDateTime.now());
        return newsMapper.newsToSimpleNewsDTO(newsRepository.save(news));
    }
}
