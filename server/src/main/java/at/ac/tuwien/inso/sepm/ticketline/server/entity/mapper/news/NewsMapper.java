package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.news;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.news.NewsSummaryMapper.NewsSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = NewsSummaryMapper.class)
public interface NewsMapper {

    News detailedNewsDTOToNews(DetailedNewsDTO detailedNewsDTO);

    DetailedNewsDTO newsToDetailedNewsDTO(News one);

    List<SimpleNewsDTO> newsToSimpleNewsDTO(List<News> all);

    @Mapping(source = "text", target = "summary", qualifiedBy = NewsSummary.class)
    SimpleNewsDTO newsToSimpleNewsDTO(News one);

}