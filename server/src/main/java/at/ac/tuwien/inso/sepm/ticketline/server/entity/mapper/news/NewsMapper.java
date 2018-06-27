package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.news;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import net.bytebuddy.dynamic.ClassFileLocator;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    /**
     * Converts a detailed news DTO to a news entity object
     *
     * @param detailedNewsDTO the object to be converted
     * @return the converted entity object
     */
    News detailedNewsDTOToNews(DetailedNewsDTO detailedNewsDTO);

    /**
     * Converts a news entity object to a detailed news DTO
     *
     * @param one the object to be converted
     * @return the converted DTO
     */
    DetailedNewsDTO newsToDetailedNewsDTO(News one);

    /**
     * Converts a simple news DTO to a news entity object
     *
     * @param simpleNewsDTO the object to be converted
     * @return the converted entity object
     */
    News simpleNewsDTOToNews(SimpleNewsDTO simpleNewsDTO);

    /**
     * Converts a list of news entity objects to a list of simple news DTOs
     *
     * @param all the list to be converted
     * @return the converted list
     */
    List<SimpleNewsDTO> newsToSimpleNewsDTO(List<News> all);

    /**
     * Converts a news entity object to a simple news DTO
     *
     * @param one the object to be converted
     * @return the converted DTO
     */
    SimpleNewsDTO newsToSimpleNewsDTO(News one);

}