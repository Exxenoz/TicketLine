package at.ac.tuwien.inso.sepm.ticketline.server.entity.specification;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;

import static com.google.common.base.Strings.isNullOrEmpty;

public class EventSpecification implements Specification<Event> {

    private final String eventName;
    private final EventTypeDTO eventType;

    public EventSpecification(String eventName, EventTypeDTO eventType) {
        this.eventName = eventName;
        this.eventType = eventType;
    }

    @Override
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return toPredicate3(root, query, builder);
    }

    public Predicate toPredicate3(From<?, Event> root, CriteriaQuery<?> query, CriteriaBuilder builder){

        final var predicates = new ArrayList<Predicate>();

        String eventNameLike = '%' + eventName + '%';
        if (!isNullOrEmpty(eventName)) {
            predicates.add(builder.like(root.get("name"), eventNameLike));
        }

        if(eventType != null){
            predicates.add(builder.equal(root.get("eventType"), EventType.from(eventType)));
        }

        return builder.and(predicates.toArray(new Predicate[]{}));

    }
}
