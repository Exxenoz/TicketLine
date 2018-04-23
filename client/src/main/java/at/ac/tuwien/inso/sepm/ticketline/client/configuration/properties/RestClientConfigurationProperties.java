package at.ac.tuwien.inso.sepm.ticketline.client.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Validated
@Configuration
@ConfigurationProperties("rest-client")
public class RestClientConfigurationProperties {

    private String userAgent;

    @NotNull
    @NestedConfigurationProperty
    private Remote remote = new Remote();

    public static class Remote {

        @NotNull
        private String protocol = "http";

        @NotNull
        private String host = "localhost";

        @NotNull
        @Min(1)
        @Max(65535)
        private Integer port = 8080;

        @NotNull
        private String path = "/";

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getFullUrl() {
            return getProtocol() + "://" +
                getHost() + ":" +
                getPort() +
                getPath();
        }
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Remote getRemote() {
        return remote;
    }

    public void setRemote(Remote remote) {
        this.remote = remote;
    }
}
