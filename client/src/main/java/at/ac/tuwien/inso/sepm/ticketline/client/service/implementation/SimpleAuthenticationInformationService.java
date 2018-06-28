package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationInformationService;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static javafx.application.Platform.runLater;

@Service
public class SimpleAuthenticationInformationService implements AuthenticationInformationService {

    private final List<AuthenticationChangeListener> changeListener = new ArrayList<>();

    private String currentAuthenticationToken;
    private AuthenticationTokenInfo currentAuthenticationTokenInfo;

    @Override
    public void setCurrentAuthenticationToken(String currentAuthenticationToken) {
        this.currentAuthenticationToken = currentAuthenticationToken;
    }

    @Override
    public void setCurrentAuthenticationTokenInfo(AuthenticationTokenInfo currentAuthenticationTokenInfo) {
        this.currentAuthenticationTokenInfo = currentAuthenticationTokenInfo;
        changeListener.forEach(authenticationChangeListener ->
            runLater(() -> authenticationChangeListener.changed(this.currentAuthenticationTokenInfo)
            ));
    }

    @Override
    public Optional<String> getCurrentAuthenticationToken() {
        return Optional.ofNullable(currentAuthenticationToken);
    }

    @Override
    public Optional<AuthenticationTokenInfo> getCurrentAuthenticationTokenInfo() {
        return ofNullable(currentAuthenticationTokenInfo);
    }

    @Override
    public void clearAuthentication() {
        currentAuthenticationToken = null;
        currentAuthenticationTokenInfo = null;
        changeListener.forEach(authenticationChangeListener ->
            authenticationChangeListener.changed(currentAuthenticationTokenInfo));
    }

    @Override
    public void addAuthenticationChangeListener(AuthenticationChangeListener changeListener) {
        this.changeListener.add(changeListener);
    }
}
