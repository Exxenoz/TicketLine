package at.ac.tuwien.inso.sepm.ticketline.server.entity.specification;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;

import static com.google.common.base.Strings.isNullOrEmpty;

public class ArtistSpecification implements Specification<Artist> {

    private final String firstName;
    private final String lastName;

    public ArtistSpecification(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public Predicate toPredicate(Root<Artist> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return toPredicate2(root, query, builder);
    }

    public Predicate toPredicate2(From<?, Artist> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        final var predicates = new ArrayList<Predicate>();

        String firstNameLike = '%' + firstName + '%';
        if (!isNullOrEmpty(firstName)) {
            predicates.add(builder.like(builder.lower(root.get("firstName")), firstNameLike.toLowerCase()));
        }

        String lastNameLike = '%' + lastName + '%';
        if (!isNullOrEmpty(lastName)) {
            predicates.add(builder.like(builder.lower(root.get("lastName")), lastNameLike.toLowerCase()));
        }
        return builder.and(predicates.toArray(new Predicate[]{}));
    }


}
