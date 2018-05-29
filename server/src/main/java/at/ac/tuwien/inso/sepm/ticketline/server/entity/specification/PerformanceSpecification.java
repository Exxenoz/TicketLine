package at.ac.tuwien.inso.sepm.ticketline.server.entity.specification;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import com.google.common.base.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.google.common.base.Strings.isNullOrEmpty;

public class PerformanceSpecification implements Specification<Performance> {

    private final BigDecimal price;
    private final LocalDateTime start;
    private final Duration duration;
    private final String locationName;
    private final String street;
    private final String city;
    private final String country;
    private final String postalCode;

    public PerformanceSpecification(BigDecimal price,
                                    LocalDateTime start,
                                    Duration duration,
                                    String locationName,
                                    String street,
                                    String city,
                                    String country,
                                    String postalCode) {
        this.price = price;
        this.start = start;
        this.duration = duration;
        this.locationName = locationName;
        this.street = street;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
    }

    @Override
    public Predicate toPredicate(Root<Performance> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        final var predicates = new ArrayList<Predicate>();

        if (start != null) {
            predicates.add(builder.equal(root.get("performanceStart"), start));
        }

        if (duration != null && !duration.isZero()) {
            Duration range = Duration.ofMinutes(30);
            Duration lower = duration.minus(range);
            Duration upper = duration.plus(range);
            predicates.add(builder.between(root.get("duration"), lower, upper));
        }

        if (price != null) {
            BigDecimal less = price.subtract(BigDecimal.valueOf(10.00));
            BigDecimal more = price.add(BigDecimal.valueOf(10.00));
            predicates.add(builder.between(root.get("price"), less, more));
        }

        if (!isNullOrEmpty(locationName)) {
            predicates.add(builder.like(root.get("locationAddress").get("locationName"), "%" + locationName + "%"));
        }

        if (!isNullOrEmpty(street)) {
            predicates.add(builder.equal(root.get("locationAddress").get("street"), street));
        }

        if (!isNullOrEmpty(city)) {
            predicates.add(builder.like(root.get("locationAddress").get("city"), "%" + city + "%"));
        }

        if (!isNullOrEmpty(country)) {
            predicates.add(builder.equal(root.get("locationAddress").get("country"), country));
        }

        if (!isNullOrEmpty(postalCode)) {
            predicates.add(builder.equal(root.get("locationAddress").get("postalCode"), postalCode));
        }

        return builder.and(predicates.toArray(new Predicate[]{}));
    }

}
