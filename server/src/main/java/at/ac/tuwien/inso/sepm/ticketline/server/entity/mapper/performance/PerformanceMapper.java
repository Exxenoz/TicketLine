package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.performance;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")

public interface PerformanceMapper {

    /**
     * Converts a performance DTO to a performance entity object
     *
     * @param performanceDTO the object to be converted
     * @return the converted entity object
     */
    Performance performanceDTOToPerformance(PerformanceDTO performanceDTO);

    /**
     * Converts a performance entity object to a performance DTO
     *
     * @param performance the object to be converted
     * @return the converted DTO
     */
    PerformanceDTO performanceToPerformanceDTO(Performance performance);

    /**
     * Converts a list of performance DTOs to a list of performance entity objects
     *
     * @param all the list to be converted
     * @return the converted list
     */
    List<Performance> performanceDTOToPerformance(List<PerformanceDTO> all);

    /**
     * Converts a list of performance entity objects to a list of performance DTOs
     *
     * @param all the list to be converted
     * @return the converted list
     */
    List<PerformanceDTO> performanceToPerformanceDTO(List<Performance> all);

}
