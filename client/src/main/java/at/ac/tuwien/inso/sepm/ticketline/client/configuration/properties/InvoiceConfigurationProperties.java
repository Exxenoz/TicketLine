package at.ac.tuwien.inso.sepm.ticketline.client.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("invoice")
public class InvoiceConfigurationProperties {

    private String location = "client-invoices-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
