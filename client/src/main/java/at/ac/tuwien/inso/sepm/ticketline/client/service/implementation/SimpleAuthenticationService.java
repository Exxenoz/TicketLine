package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.AuthenticationRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationInformationService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationService;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationRequest;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationToken;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ScheduledFuture;

@Service
public class SimpleAuthenticationService implements AuthenticationService, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final AuthenticationRestClient authenticationRestClient;
    private final AuthenticationInformationService authenticationInformationService;
    private ThreadPoolTaskScheduler taskScheduler;

    private AuthenticationToken authenticationToken;
    private ScheduledFuture<?> schedule;

    public SimpleAuthenticationService(
        AuthenticationRestClient authenticationRestClient,
        AuthenticationInformationService authenticationInformationService
    ) {
        this.authenticationInformationService = authenticationInformationService;
        this.authenticationRestClient = authenticationRestClient;
        prepareAuthenticationContext();
    }

    @Override
    public AuthenticationTokenInfo authenticate(
        AuthenticationRequest authenticationRequest
    ) throws DataAccessException {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Trying to authenticate {}", authenticationRequest);
        } else {
            LOGGER.debug("Trying to authenticate");
        }
        return authenticateAndScheduleNextAuthentication(authenticationRequest);
    }

    private AuthenticationTokenInfo authenticateAndScheduleNextAuthentication() throws DataAccessException {
        return authenticateAndScheduleNextAuthentication(null);
    }

    private AuthenticationTokenInfo authenticateAndScheduleNextAuthentication(
        AuthenticationRequest authenticationRequest
    ) throws DataAccessException {
        if (null == authenticationRequest) {
            authenticationToken = authenticationRestClient.authenticate();
        } else {
            authenticationToken = authenticationRestClient.authenticate(authenticationRequest);
        }
        LOGGER.debug("Authentication result {}", authenticationToken);
        authenticationInformationService.setCurrentAuthenticationToken(authenticationToken.getCurrentToken());
        final var authenticationTokenInfo = authenticationRestClient.tokenInfoCurrent();
        scheduleReAuthenticationTask(authenticationTokenInfo
            .getExpireAt()
            .minus(authenticationTokenInfo.getOverlapDuration().dividedBy(2)));
        authenticationInformationService.setCurrentAuthenticationTokenInfo(authenticationTokenInfo);
        return authenticationTokenInfo;
    }

    private void scheduleReAuthenticationTask(LocalDateTime runAt) {
        schedule = taskScheduler.schedule(
            () -> {
                LOGGER.debug("Setting current token to future token");
                authenticationInformationService.setCurrentAuthenticationToken(authenticationToken.getFutureToken());
                LOGGER.debug("Trying to re-authenticate {}", authenticationToken);
                try {
                    authenticateAndScheduleNextAuthentication();
                } catch (DataAccessException e) {
                    deAuthenticate();
                }
            },
            Date.from(runAt.atZone(ZoneId.systemDefault()).toInstant()));
    }

    @Override
    public void deAuthenticate() {
        LOGGER.debug("De authenticating");
        authenticationInformationService.clearAuthentication();
        authenticationToken = null;
        taskScheduler.shutdown();

        if (schedule != null) {
            schedule.cancel(true);
        }
    }

    @Override
    public void destroy() {
        deAuthenticate();
    }

    @Override
    public void prepareAuthenticationContext() {
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.initialize();
    }
}
