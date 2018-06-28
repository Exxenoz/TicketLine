package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import org.springframework.beans.factory.InitializingBean;

public interface DataGenerator extends InitializingBean {

    @Override
    default void afterPropertiesSet() {
        this.generate();
    }

    /**
     * generates the necessary data
     */
    void generate();
}
