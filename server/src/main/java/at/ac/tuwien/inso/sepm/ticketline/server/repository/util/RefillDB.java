package at.ac.tuwien.inso.sepm.ticketline.server.repository.util;

import at.ac.tuwien.inso.sepm.ticketline.server.datagenerator.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class RefillDB {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RefillDB.class, args);

        EventRepository eventRepository = context.getBean(EventRepository.class);
        ArtistRepository artistRepository = context.getBean(ArtistRepository.class);
        NewsRepository newsRepository = context.getBean(NewsRepository.class);
        PerformanceRepository performanceRepository = context.getBean(PerformanceRepository.class);
        ReservationRepository reservationRepository = context.getBean(ReservationRepository.class);
        SeatRepository seatRepository = context.getBean(SeatRepository.class);
        SectorCategoryRepository sectorCategoryRepository = context.getBean(SectorCategoryRepository.class);
        SectorRepository sectorRepository = context.getBean(SectorRepository.class);
        UserRepository usersRepository = context.getBean(UserRepository.class);

        //conditional beans => not in context
        List<DataGenerator> generators = List.of(
            new SectorCategoryDataGenerator(sectorCategoryRepository),
            new SectorDataGenerator(sectorRepository, sectorCategoryRepository),
            new SeatDataGenerator(seatRepository, sectorRepository),
            new ArtistDataGenerator(artistRepository),
            new EventDataGenerator(eventRepository, artistRepository),
            new PerformanceDataGenerator(performanceRepository, eventRepository),
            new NewsDataGenerator(newsRepository),
            new ReservationDataGenerator(reservationRepository, seatRepository, performanceRepository)
        );
        CompositeDataGenerator compositeDataGenerator = new CompositeDataGenerator(generators);

        reservationRepository.deleteAll();
        seatRepository.deleteAll();
        sectorRepository.deleteAll();
        sectorCategoryRepository.deleteAll();
        performanceRepository.deleteAll();
        eventRepository.deleteAll();
        artistRepository.deleteAll();
        newsRepository.deleteAll();
        usersRepository.deleteAll();

        //CompositeDataGenerator
        compositeDataGenerator.generate();
        System.exit(0);
    }
}
