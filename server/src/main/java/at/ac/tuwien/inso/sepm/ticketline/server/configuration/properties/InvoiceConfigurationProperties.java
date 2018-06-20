package at.ac.tuwien.inso.sepm.ticketline.server.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("invoice")
public class InvoiceConfigurationProperties {

    private String location = "invoices-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}