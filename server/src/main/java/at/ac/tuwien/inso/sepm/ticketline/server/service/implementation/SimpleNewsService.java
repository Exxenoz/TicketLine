package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalNotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.NewsRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.NewsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SimpleNewsService implements NewsService {

    private final NewsRepository newsRepository;

    public SimpleNewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public List<News> findAll() {
        return newsRepository.findAllByOrderByPublishedAtDesc();
    }

    @Override
    public News findOne(Long id) throws InternalNotFoundException {
        return newsRepository.findOneById(id).orElseThrow(InternalNotFoundException::new);
    }

    @Override
    public News publishNews(News news) {
        news.setPublishedAt(LocalDateTime.now());
        return newsRepository.save(news);
    }

}
