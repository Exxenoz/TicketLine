package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.news;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import net.bytebuddy.dynamic.ClassFileLocator;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    News detailedNewsDTOToNews(DetailedNewsDTO detailedNewsDTO);

    DetailedNewsDTO newsToDetailedNewsDTO(News one);

    News simpleNewsDTOToNews(SimpleNewsDTO simpleNewsDTO);

    List<SimpleNewsDTO> newsToSimpleNewsDTO(List<News> all);

    SimpleNewsDTO newsToSimpleNewsDTO(News one);

}