package at.ac.tuwien.inso.sepm.ticketline.server.unittest.events;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Address;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PerformanceService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@ActiveProfiles("unit-test")
public class PerformanceServiceTests {

    @Autowired
    PerformanceService service;

    @Autowired
    public static PerformanceRepository repository;

    @BeforeClass
    public static void setup() {
        //Setup some performances
        //First performance
        Address address = new Address("Staatsoper",
            "Herbert-Karajan-Platz",
            "Vienna",
            "Austria",
            "1010");
   /*     Performance p = new Performance(null,
            "Zauberflöte",
            20.0,
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
            15.0,
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
            9.0,
            LocalDateTime.now().plusDays(10),
            LocalDateTime.now().plusDays(10).plusHours(2),
            address
        );
        repository.save(p); */
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
}
