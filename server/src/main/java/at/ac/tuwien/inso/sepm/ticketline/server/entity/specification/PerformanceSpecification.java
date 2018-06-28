package at.ac.tuwien.inso.sepm.ticketline.server.entity.specification;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static com.google.common.base.Strings.isNullOrEmpty;

public class PerformanceSpecification implements Specification<Performance> {

    /**
     * Lower boundary for filtering by price, in this case up to 10€ less.
     */
    private final static Long PRICE_LOWER_BOUND_RANGE = 1000L;

    /**
     * Upper Boundary for filtering by price, in this case up to 10€ more
     */
    private final static Long PRICE_UPPER_BOUND_RANGE = 1000L;

    private final String performanceName;
    private final Long price;
    private final LocalDateTime start;
    private final Duration duration;
    private final String locationName;
    private final String street;
    private final String city;
    private final String country;
    private final String postalCode;

    public PerformanceSpecification(String performanceName,
                                    Long price,
                                    LocalDateTime start,
                                    Duration duration,
                                    String locationName,
                                    String street,
                                    String city,
                                    String country,
                                    String postalCode) {
        this.performanceName = performanceName;
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

            LocalDate chosenDate = LocalDate.of(start.getYear(), start.getMonth(), start.getDayOfMonth());
            LocalDateTime before;
            LocalDateTime after;

            if(start.isEqual(LocalDateTime.of(chosenDate, LocalTime.MIDNIGHT))){
                before = start;
                after = start.plusDays(1).minusNanos(1);

                predicates.add(builder.between(root.get("performanceStart"), before, after));

            } else {

                before = start.minusHours(1);
                after = start.plusHours(1);

                predicates.add(builder.between(root.get("performanceStart"), before, after));
            }
        }

        if (duration != null && !duration.isZero()) {
            Duration range = Duration.ofMinutes(30);
            Duration lower = duration.minus(range);
            Duration upper = duration.plus(range);
            predicates.add(builder.between(root.get("duration"), lower, upper));
        }

        if (price != null) {
            Long less = price - PRICE_LOWER_BOUND_RANGE;
            Long more = price + PRICE_UPPER_BOUND_RANGE;
            predicates.add(builder.between(root.get("price"), less, more));
        }

        Path<Object> locationAddress = root.get("locationAddress");

        if (!isNullOrEmpty(locationName)) {
            predicates.add(fulltextSearch(builder, locationAddress.get("locationName"), locationName));
        }

        if (!isNullOrEmpty(street)) {
            predicates.add(fulltextSearch(builder, locationAddress.get("street"), street));
        }

        if (!isNullOrEmpty(city)) {
            predicates.add(fulltextSearch(builder, locationAddress.get("city"), city));
        }

        if (!isNullOrEmpty(country)) {
            predicates.add(fulltextSearch(builder, locationAddress.get("country"), country));
        }

        if (!isNullOrEmpty(postalCode)) {
            predicates.add(fulltextSearch(builder, locationAddress.get("postalCode"), postalCode));
        }

        if (!isNullOrEmpty(performanceName)) {
            predicates.add(fulltextSearch(builder, root.get("name"), performanceName));
        }

        return builder.and(predicates.toArray(new Predicate[]{}));
    }

    private static Predicate fulltextSearch(CriteriaBuilder builder, Path<String> fieldPath, String searchString) {
        return builder.like(builder.lower(fieldPath), ("%" + searchString + "%").toLowerCase());
    }
}
