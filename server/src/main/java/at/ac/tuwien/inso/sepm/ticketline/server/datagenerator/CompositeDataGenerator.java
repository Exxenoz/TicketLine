package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("generateData")
@Component
public class CompositeDataGenerator implements InitializingBean {

    private final List<DataGenerator> generators;

    public CompositeDataGenerator(List<DataGenerator> generators) {
        this.generators = generators;
    }


    @Override
    public void afterPropertiesSet() {
        generators.forEach(DataGenerator::generate);
    }
}
