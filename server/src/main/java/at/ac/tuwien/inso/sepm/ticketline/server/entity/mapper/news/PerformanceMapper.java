package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.news;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PerformanceMapper {

    Performance performanceDTOToPerformance(PerformanceDTO performanceDTO);

    PerformanceDTO performanceToPerformanceDTO(Performance performance);

    List<PerformanceDTO> performanceToPerformanceDTO(List<Performance> all);

}
