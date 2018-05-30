package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;


@Profile("generateData")
@Component
public class UserDataGenerator implements DataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_USERS_TO_GENERATE = 50;
    private static long appendix = 0;
    private final UserRepository userRepository;
    private final Faker faker;

    public UserDataGenerator(UserRepository userRepository) {
        this.userRepository = userRepository;
        faker = new Faker();
    }

    @Override
    public void generate() {
        if (userRepository.count() > 4) {
            LOGGER.info("users already generated");
        } else {
            LOGGER.info("generating {} user entries", NUMBER_OF_USERS_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_USERS_TO_GENERATE; i++) {
                String username = faker.name().firstName();
                if (userRepository.findByUsername(username) != null) {
                    appendix++;
                    username += appendix;
                }
                var user = User.builder()
                    .username(username)
                    .password("$2a$10$hXJx1IBhxH2fcTa/NR2ZMetAKy.4w3SoWeJm7FiEjK6XjOOtyRQmO")
                    .enabled(true)
                    .strikes(0)
                    .roles(new HashSet<>()).build();
                userRepository.save(user);
            }
        }
    }
}
