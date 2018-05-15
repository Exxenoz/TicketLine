package at.ac.tuwien.inso.sepm.ticketline.server;

import at.ac.tuwien.inso.sepm.ticketline.server.datagenerator.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class RefillDB {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RefillDB.class, args);

        EventRepository eventRepository = context.getBean(EventRepository.class);
        NewsRepository newsRepository = context.getBean(NewsRepository.class);
        PerformanceRepository performanceRepository = context.getBean(PerformanceRepository.class);
        ReservationRepository reservationRepository = context.getBean(ReservationRepository.class);
        SeatRepository seatRepository = context.getBean(SeatRepository.class);
        SectorCategoryRepository sectorCategoryRepository = context.getBean(SectorCategoryRepository.class);
        SectorRepository sectorRepository = context.getBean(SectorRepository.class);
        UsersRepository usersRepository = context.getBean(UsersRepository.class);

        //conditional beans => not in context
        List<DataGenerator> generators = List.of(
            new SectorCategoryDataGenerator(sectorCategoryRepository),
            new SectorDataGenerator(sectorRepository, sectorCategoryRepository),
            new SeatDataGenerator(seatRepository, sectorRepository),
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
        newsRepository.deleteAll();
        usersRepository.deleteAll();

        //CompositeDataGenerator
        compositeDataGenerator.generate();
    }
}
