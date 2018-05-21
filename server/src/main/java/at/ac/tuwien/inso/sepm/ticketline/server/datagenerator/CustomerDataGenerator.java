package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
public class CustomerDataGenerator implements DataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final int NUMBER_OF_CUSTOMERS_TO_GENERATE = 50;

    private final CustomerRepository customerRepository;
    private final Faker faker;

    public CustomerDataGenerator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.faker = new Faker();
    }

    @Override
    public void generate() {
        if (customerRepository.count() > 0) {
            LOGGER.info("customers already generated");
        } else {
            LOGGER.info("generating {} customer entries", NUMBER_OF_CUSTOMERS_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_CUSTOMERS_TO_GENERATE; i++) {
                final var customer = Customer.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .telephoneNumber(faker.phoneNumber().phoneNumber())
                    .email(faker.internet().emailAddress())
                    .address(new Address(faker.lorem().characters(3, 25), faker.lorem().characters(3, 25), faker.lorem().characters(3, 25), faker.lorem().characters(3, 25), faker.lorem().characters(4, 5)))
                    .build();

                LOGGER.debug("saving customer {}", customer);

                customerRepository.save(customer);
            }
        }
    }
}
