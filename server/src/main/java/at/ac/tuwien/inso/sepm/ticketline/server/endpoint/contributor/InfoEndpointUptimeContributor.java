package at.ac.tuwien.inso.sepm.ticketline.server.endpoint.contributor;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.MILLIS;

@Component
public class InfoEndpointUptimeContributor implements InfoContributor {

    private final RuntimeMXBean runtimeMXBean;

    public InfoEndpointUptimeContributor() {
        runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    }

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("uptime", Duration.of(runtimeMXBean.getUptime(), MILLIS));
    }
}
