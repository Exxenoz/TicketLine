package at.ac.tuwien.inso.sepm.ticketline.server.unittests.events;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ArtistRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PerformanceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("unit-test")
public class PerformanceServiceTests {

    private static final Event EVENT = new Event("Event", EventType.SECTOR, "description");

    private static final Artist ARTIST_1 = new Artist("artist A", "0");
    private static final Artist ARTIST_2 = new Artist("artist B", "1");
    private static final Artist[] artistArray_1 = new Artist[]{ARTIST_1, ARTIST_2};
    private static final Set<Artist> ARTISTS_1 = new HashSet<>(Arrays.asList(artistArray_1));

    private static final Artist ARTIST_3 = new Artist("artist C", "0");
    private static final Artist ARTIST_4 = new Artist("artist D", "1");
    private static final Artist[] artistArray_2 = new Artist[]{ARTIST_3, ARTIST_4};
    private static final Set<Artist> ARTISTS_2 = new HashSet<>(Arrays.asList(artistArray_2));


    private static final Address ADDRESS_1 = new Address("Staatsoper",
        "Herbert-Karajan-Platz",
        "Vienna",
        "Austria",
        "1010");
    private static final Performance PERFORMANCE_1 = new Performance(EVENT, ARTISTS_1,
        "Zauberflöte",
        //be careful with BigDecimal - 20 != 20.00 - equals will fail
        new BigDecimal("20.00"),
        LocalDateTime.now().plusDays(5).withNano(0),
        LocalDateTime.now().plusDays(5).plusHours(2).withNano(0),
        ADDRESS_1
    );

    private static final Address ADDRESS_2 = new Address("Konzerthaus",
        "Ring",
        "Vienna",
        "Austria",
        "1010");
    private static final Performance PERFORMANCE_2 = new Performance(EVENT, ARTISTS_2,
        "Vulfpeck Live",
        //be careful with BigDecimal - 20 != 20.00 - equals will fail
        new BigDecimal("20.00"),
        LocalDateTime.now().plusDays(20).withNano(0),
        LocalDateTime.now().plusDays(20).plusHours(3).withNano(0),
        ADDRESS_2
    );

    private static final Address ADDRESS_3 = new Address("Konzerthaus",
        "Ring",
        "Vienna",
        "Austria",
        "1010");
    private static final Performance PERFORMANCE_3 = new Performance(EVENT, ARTISTS_1,
        "Avishai Cohen",
        //be careful with BigDecimal - 20 != 20.00 - equals will fail
        new BigDecimal("20.00"),
        LocalDateTime.now().plusDays(10).withNano(0),
        LocalDateTime.now().plusDays(10).plusHours(2).withNano(0),
        ADDRESS_3
    );

    @Autowired
    private PerformanceService service;
    @Autowired
    private PerformanceRepository repository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ArtistRepository artistRepository;

    @Test
    public void findAll() {
        createPerformances();

        List<Performance> performances = service.findAll();

        assertThat(performances).containsExactlyInAnyOrder(PERFORMANCE_1, PERFORMANCE_2, PERFORMANCE_3);
    }

    @Test
    public void simpleSearchByPerformanceName() {
        createPerformances();
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setPerformanceName("zauberflöte");

        List<Performance> performances = service.search(searchDTO);

        assertThat(performances).containsExactly(PERFORMANCE_1);
    }

    @Test
    public void searchByCity() {
        createPerformances();
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setCity("Vienna");

        List<Performance> performances = service.search(searchDTO);

        assertThat(performances).containsExactlyInAnyOrder(PERFORMANCE_1, PERFORMANCE_2, PERFORMANCE_3);
    }

    @Test
    public void searchByFutureDate() {
        createPerformances();
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setPerformanceStart(LocalDateTime.now().plusDays(22));

        List<Performance> performances = service.search(searchDTO);

        assertThat(performances).doesNotContain(PERFORMANCE_1, PERFORMANCE_2, PERFORMANCE_3);
    }

    @Test
    public void searchByArtistFirstName() {
        createPerformances();
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setArtistFirstName("artist C");

        List<Performance> performances = service.search(searchDTO);

        assertThat(performances).containsExactly(PERFORMANCE_2);
    }

    private void createPerformances() {
        eventRepository.save(EVENT);
        artistRepository.saveAll(List.of(ARTIST_1, ARTIST_2, ARTIST_3, ARTIST_4));
        repository.saveAll(List.of(PERFORMANCE_1, PERFORMANCE_2, PERFORMANCE_3));
    }

}
