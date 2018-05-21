package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PerformanceService;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SimplePerformanceService implements PerformanceService {

    private final PerformanceRepository performanceRepository;

    public SimplePerformanceService(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    @Override
    public List<Performance> findAll() {
        return performanceRepository.findAll();
    }

    @Override
    public List<Performance> findByEventID(Long eventID) {
        return performanceRepository.findByEventId(eventID);
    }

    @Override
    public List<Performance> search(SearchDTO search) {
        Example<Performance> example = getPerformanceExample(search);

        return performanceRepository.findAll(example);
    }

    //building example for search
    private Example<Performance> getPerformanceExample(SearchDTO search) {

        Performance performance = new Performance();
        performance.setName(search.getPerformanceName());
        performance.setPerformanceStart(search.getPerformanceStart());

        //adding duration to performance start in order to get performance end
        LocalDateTime performanceEnd = null;
        if (performance.getPerformanceStart() != null && search.getDuration() != null) {
            performanceEnd = performance.getPerformanceStart().plus(search.getDuration());
        }
        performance.setPerformanceEnd(performanceEnd);
        performance.setPrice(search.getPrice());

        Artist artist = new Artist(search.getArtistFirstName(), search.getArtistLastName());
        Set<Artist> artists = new HashSet<>();
        artists.add(artist);

        Event event = new Event();
        event.setName(search.getEventName());

        if (search.getEventType() != null) {
            event.setEventType(EventType.valueOf(search.getEventType().toString()));
        }

        Address address = new Address();
        address.setLocationName(search.getLocationName());
        address.setStreet(search.getStreet());
        address.setCity(search.getCity());
        address.setCountry(search.getCountry());
        address.setPostalCode(search.getPostalCode());

        performance.setEvent(event);
        performance.setArtists(artists);
        performance.setAddress(address);

        //adding special checks - no case sensitivity, allow searching for parts of names
        ExampleMatcher matcher = ExampleMatcher.matching()
            // .withMatcher("performanceStart", ExampleMatcher.)
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreCase();

        return Example.of(performance, matcher);
    }
}
