package at.ac.tuwien.inso.sepm.ticketline.server.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("price.calculation")
public class PriceCalculationProperties {

    private double salesTax = 0.2;

    public double getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(double salesTax) {
        this.salesTax = salesTax;
    }
}
