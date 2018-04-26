package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.configuration.properties.RestClientConfigurationProperties;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.InfoRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.info.Info;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class AboutController {

    @FXML
    private Label labClientVersion;
    @FXML
    private Label labClientBuildTime;
    @FXML
    private Label labClientTags;
    @FXML
    private Label labClientCommit;
    @FXML
    private Label labClientCommitTime;
    @FXML
    private Label labClientBranch;
    @FXML
    private Label labClientUptime;

    @FXML
    private Label labServerAddress;

    @FXML
    private Label labServerVersion;
    @FXML
    private Label labServerBuildTime;
    @FXML
    private Label labServerTags;
    @FXML
    private Label labServerCommit;
    @FXML
    private Label labServerCommitTime;
    @FXML
    private Label labServerBranch;
    @FXML
    private Label labServerUptime;

    @FXML
    private TicketlineInfoController ticketlineInfoController;

    private static final DateTimeFormatter ISO_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final GitProperties gitProperties;
    private final RestClientConfigurationProperties restClientConfigurationProperties;
    private final InfoRestClient infoRestClient;
    private final RuntimeMXBean runtimeMXBean;

    public AboutController(
        GitProperties gitProperties,
        RestClientConfigurationProperties restClientConfigurationProperties,
        InfoRestClient infoRestClient
    ) {
        this.gitProperties = gitProperties;
        this.restClientConfigurationProperties = restClientConfigurationProperties;
        this.infoRestClient = infoRestClient;
        runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    }

    @FXML
    private void initialize() {
        labClientVersion.setText(gitProperties.get("build.version"));
        final var buildTimeString = gitProperties.get("build.time");
        LocalDateTime buildTime = null;
        if (buildTimeString != null && !buildTimeString.isEmpty()) {
            buildTime = ZonedDateTime.parse(buildTimeString, ISO_DATETIME_FORMATTER)
                .withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        }
        labClientBuildTime.setText((buildTime != null) ? DATETIME_FORMATTER.format(buildTime) : "-");
        labClientCommit.setText(gitProperties.get("commit.id.abbrev"));
        final var commitTimeString = gitProperties.get("commit.time");
        LocalDateTime commitTime = null;
        if (commitTimeString != null && !commitTimeString.isEmpty()) {
            commitTime = ZonedDateTime.parse(commitTimeString, ISO_DATETIME_FORMATTER)
                .withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        }
        labClientCommitTime.setText((commitTime != null) ? DATETIME_FORMATTER.format(commitTime) : "-");
        labClientBranch.setText(gitProperties.get("branch"));
        final var tags = gitProperties.get("tags");
        labClientTags.setText((tags != null && !tags.isEmpty()) ? tags : "-");
        labClientUptime.setText(formatDuration(Duration.of(runtimeMXBean.getUptime(), ChronoUnit.MILLIS)));

        labServerAddress.setText(restClientConfigurationProperties.getRemote().getFullUrl());

        labServerVersion.setText("-");
        labServerBuildTime.setText("-");
        labServerCommit.setText("-");
        labServerCommitTime.setText("-");
        labServerBranch.setText("-");
        labServerTags.setText("-");
        labServerUptime.setText("-");

        final var task = new Task<Info>() {
            @Override
            protected Info call() throws DataAccessException {
                return infoRestClient.find();
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                final var info = getValue();
                if (info != null) {
                    final var git = info.getGit();
                    if (git != null) {
                        final var build = git.getBuild();
                        if (build != null) {
                            labServerVersion.setText((build.getVersion() != null) ? build.getVersion() : "-");
                            labServerBuildTime.setText((build.getTime() != null) ? DATETIME_FORMATTER.format(
                                build.getTime().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()) : "-");
                        }
                        final var commit = git.getCommit();
                        if (commit != null) {
                            labServerCommit.setText(
                                (commit.getId() != null && commit.getId().getAbbrev() != null) ?
                                    commit.getId().getAbbrev() : "-");
                            labServerCommitTime.setText((commit.getTime() != null) ? DATETIME_FORMATTER.format(
                                commit.getTime().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()) : "-");
                        }
                        final var branch = git.getBranch();
                        labServerBranch.setText((branch != null && !branch.isEmpty()) ? branch : "-");
                        final var tags = git.getTags();
                        labServerTags.setText((tags != null && !tags.isEmpty()) ? tags : "-");
                    }
                    final var uptime = info.getUptime();
                    labServerUptime.setText((uptime != null) ? formatDuration(uptime) : "-");
                }
            }
        };
        new Thread(task).start();

        ticketlineInfoController.setBuildDateTime((buildTime != null) ? buildTime : LocalDateTime.now());
        ticketlineInfoController.setVersion(gitProperties.get("build.version"));
        ticketlineInfoController.setInfoText("Client & Server Information");
    }

    private static String formatDuration(Duration duration) {
        if (duration == null) {
            return "-";
        }
        final var seconds = duration.getSeconds();
        final var absSeconds = Math.abs(seconds);
        final var positive = String.format(
            "%02d:%02d:%02d:%02d",
            absSeconds / 86400,
            (absSeconds % 86400) / 3600,
            (absSeconds % 3600) / 60,
            absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }
}
