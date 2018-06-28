package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.specification.ArtistSpecification;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.specification.EventSpecification;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.specification.PerformanceSpecification;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.specification.SearchSpecBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long>, JpaSpecificationExecutor<Performance> {

    /**
     * Finds a list of performances filtered by the given event id.
     *
     * @param eventID event id
     * @return all performances of the given event
     */
    Page<Performance> findByEventId(Long eventID, Pageable pageable);

    /**
     * Finds a list of all performances that match the given criteria
     *
     * @param searchDTO holds all given criteria
     * @return all performances that match the given criteria
     */
    default Page<Performance> findAll(SearchDTO searchDTO, Pageable pageable) {
        var artistSpecs = new ArtistSpecification(searchDTO.getFirstName(), searchDTO.getLastName());
        var eventSpecs = new EventSpecification(searchDTO.getEventName(), searchDTO.getEventType());
        var performanceSpecs = new PerformanceSpecification(
            searchDTO.getPerformanceName(),
            searchDTO.getPrice(),
            searchDTO.getPerformanceStart(),
            searchDTO.getDuration(),
            searchDTO.getLocationName(),
            searchDTO.getStreet(),
            searchDTO.getCity(),
            searchDTO.getCountry(),
            searchDTO.getPostalCode());

        SearchSpecBuilder searchSpecBuilder = new SearchSpecBuilder(artistSpecs, eventSpecs, performanceSpecs);
        return findAll(searchSpecBuilder, pageable);
    }
}
