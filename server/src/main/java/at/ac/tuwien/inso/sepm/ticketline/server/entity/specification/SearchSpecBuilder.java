package at.ac.tuwien.inso.sepm.ticketline.server.entity.specification;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class SearchSpecBuilder implements Specification<Performance> {

    private final ArtistSpecification artistSpecification;
    private final EventSpecification eventSpecification;
    private final PerformanceSpecification performanceSpecification;

    public SearchSpecBuilder(ArtistSpecification artistSpecification,
                             EventSpecification eventSpecification,
                             PerformanceSpecification performanceSpecification) {
        this.artistSpecification = artistSpecification;
        this.eventSpecification = eventSpecification;
        this.performanceSpecification = performanceSpecification;
    }

    @Override
    public Predicate toPredicate(Root<Performance> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        query.distinct(true);

        Join<Performance, Event> event = root.join("event");
        SetJoin<Performance, Artist> artists = root.joinSet("artists", JoinType.LEFT);

        return builder.and(
            performanceSpecification.toPredicate(root, query, builder),
            artistSpecification.toPredicate2(artists, query, builder)
        );
    }
}
