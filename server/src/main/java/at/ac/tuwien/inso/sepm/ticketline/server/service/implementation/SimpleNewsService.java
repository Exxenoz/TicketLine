package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.NewsValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.NewsValidator;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.news.NewsMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.NewsRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.security.IAuthenticationFacade;
import at.ac.tuwien.inso.sepm.ticketline.server.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SimpleNewsService implements NewsService {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final NewsMapper newsMapper;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    public SimpleNewsService(NewsRepository newsRepository, UserRepository userRepository, NewsMapper newsMapper) {
        this.newsRepository = newsRepository;
        this.userRepository = userRepository;
        this.newsMapper = newsMapper;
    }

    @Override
    public List<News> findAll() {
        LOGGER.info("Get all News");
        return newsRepository.findAllByOrderByPublishedAtDesc();
    }

    @Override
    public PageResponseDTO<SimpleNewsDTO> findAllUnread(PageRequestDTO pageRequestDTO) throws InternalForbiddenException, InternalBadRequestException {
        LOGGER.info("Get Page {} of unread News", pageRequestDTO.getPageable().getPageNumber());
        Authentication authentication = authenticationFacade.getAuthentication();

        if (authentication == null) {
            LOGGER.error("Missing Authentication for getting News");
            throw new InternalForbiddenException();
        }

        if (pageRequestDTO == null) {
            LOGGER.warn("PageRequest can not be null");
            throw new InternalBadRequestException();
        }

        if (pageRequestDTO.getPage() < 0) {
            LOGGER.warn("The Requested Page can not be negative");
            throw new InternalBadRequestException();
        }

        if (pageRequestDTO.getSize() < 1) {
            LOGGER.warn("The Requested Page size should be at least one");
            throw new InternalBadRequestException();
        }

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize(), Sort.by(Sort.Direction.DESC, "publishedAt"));
        Page<News> newsPage = newsRepository.findAllUnreadByUsername(authentication.getName(), pageable);
        return new PageResponseDTO<>(newsMapper.newsToSimpleNewsDTO(newsPage.getContent()), newsPage.getTotalPages());
    }

    @Override
    public PageResponseDTO<SimpleNewsDTO> findAllRead(PageRequestDTO pageRequestDTO) throws InternalForbiddenException, InternalBadRequestException {
        LOGGER.info("Get Page {} of read News", pageRequestDTO.getPageable().getPageNumber());
        Authentication authentication = authenticationFacade.getAuthentication();

        if (authentication == null) {
            LOGGER.error("Missing Authentication for getting News");
            throw new InternalForbiddenException();
        }

        if (pageRequestDTO == null) {
            LOGGER.warn("PageRequest can not be null");
            throw new InternalBadRequestException();
        }

        if (pageRequestDTO.getPage() < 0) {
            LOGGER.warn("The Requested Page can not be negative");
            throw new InternalBadRequestException();
        }

        if (pageRequestDTO.getSize() < 1) {
            LOGGER.warn("The Requested Page size should be at least one");
            throw new InternalBadRequestException();
        }

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize(), Sort.by(Sort.Direction.DESC, "publishedAt"));
        Page<News> newsPage = newsRepository.findAllReadByUsername(authentication.getName(), pageable);
        return new PageResponseDTO<>(newsMapper.newsToSimpleNewsDTO(newsPage.getContent()), newsPage.getTotalPages());
    }

    @Override
    public DetailedNewsDTO findOne(Long id) throws InternalNotFoundException {
        LOGGER.info("Get detailed News with  id={}", id);
        News news = newsRepository.findOneById(id).orElseThrow(InternalNotFoundException::new);
        return newsMapper.newsToDetailedNewsDTO(news);
    }

    @Override
    public SimpleNewsDTO publishNews(DetailedNewsDTO detailedNewsDTO) throws InternalNewsValidationException {
        LOGGER.info("Publish News {}", detailedNewsDTO);
        try {
            NewsValidator.validateNews(detailedNewsDTO);
        } catch (NewsValidationException e) {
            LOGGER.warn("News was invalid: {}", e.getMessage());
            throw new InternalNewsValidationException();
        }

        News news = newsMapper.detailedNewsDTOToNews(detailedNewsDTO);
        news.setPublishedAt(LocalDateTime.now());
        news = newsRepository.save(news);
        LOGGER.debug("Successfully published News {}", news);
        return newsMapper.newsToSimpleNewsDTO(news);
    }

    @Override
    public void markAsRead(Long id) throws InternalNotFoundException, InternalForbiddenException, InternalUserNotFoundException {
        LOGGER.info("Mark the News with id={} as read", id);
        News news = newsRepository.findOneById(id).orElseThrow(InternalNotFoundException::new);
        Authentication authentication = authenticationFacade.getAuthentication();

        if (authentication == null) {
            LOGGER.error("Missing authentication");
            throw new InternalForbiddenException();
        }

        User user = userRepository.findByUsername(authentication.getName());

        if (user == null) {
            LOGGER.error("Could not find authenticated User \'{}\'", authentication.getName());
            throw new InternalUserNotFoundException();
        }

        user.getReadNews().add(news);
        userRepository.save(user);
        LOGGER.debug("Successfully marked News {} as read for User {}", news, user);
    }
}
