package at.ac.tuwien.inso.sepm.ticketline.server.unittests.events;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Address;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PerformanceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("unit-test")
public class PerformanceServiceTests {

    /**
     * @BeforeClass does not work with @Autowired and static members
     * so we have to use some dirty tricks
     */
    private boolean dataLoaded = false;

    @Autowired
    PerformanceService service;

    @Autowired
    public PerformanceRepository repository;

    @Before
    public void setup() {
        if(!dataLoaded) {
            dataLoaded = true;
            //Setup some performances
            //First performance
            Address address = new Address("Staatsoper",
                "Herbert-Karajan-Platz",
                "Vienna",
                "Austria",
                "1010");

            Performance p = new Performance(null,
                "Zauberflöte",
                new BigDecimal(20),
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(2),
                address
            );
            repository.save(p);

            //Second performance
            address = new Address("Konzerthaus",
                "Ring",
                "Vienna",
                "Austria",
                "1010");
            p = new Performance(null,
                "Vulfpeck Live",
                new BigDecimal(20),
                LocalDateTime.now().plusDays(20),
                LocalDateTime.now().plusDays(20).plusHours(3),
                address
            );
            repository.save(p);

            //Third performance
            address = new Address("Konzerthaus",
                "Ring",
                "Vienna",
                "Austria",
                "1010");
            p = new Performance(null,
                "Avishai Cohen",
                new BigDecimal(20),
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(10).plusHours(2),
                address
            );
            repository.save(p);
        }
    }

    @Test
    public void simpleSearchByPerformanceName() {
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setEventName("zauberflöte");

        List<Performance> performances = service.search(searchDTO);
        Assert.assertTrue(performances.size() == 1);
        Assert.assertTrue(performances.get(0).getName().equals("Zauberflöte"));
    }

    @Test
    public void searchByCity() {
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setCity("Vienna");

        List<Performance> performances = service.search(searchDTO);
        Assert.assertTrue(performances.size() == 3);

        for(Performance p: performances) {
            Assert.assertTrue(p.getAddress().getCity().equals("Vienna"));
        }
    }

    @Test
    public void searchByFutureDate() {
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setPerformanceStart(LocalDateTime.now().plusDays(22));

        List<Performance> performances = service.search(searchDTO);
        Assert.assertTrue(performances.size() == 0);
    }

    @Test
    public void searchByArtistName() {
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setArtistFirstName("Vulfpeck");

        List<Performance> performances = service.search(searchDTO);
        Assert.assertTrue(performances.size() == 1);
    }
}
