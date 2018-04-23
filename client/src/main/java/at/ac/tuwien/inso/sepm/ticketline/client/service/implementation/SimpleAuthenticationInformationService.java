package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationInformationService;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import javafx.application.Platform;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SimpleAuthenticationInformationService implements AuthenticationInformationService {

    private String currentAuthenticationToken;

    private AuthenticationTokenInfo currentAuthenticationTokenInfo;
    private List<AuthenticationChangeListener> changeListener = new ArrayList<>();

    @Override
    public void setCurrentAuthenticationToken(String currentAuthenticationToken) {
        this.currentAuthenticationToken = currentAuthenticationToken;
    }

    @Override
    public void setCurrentAuthenticationTokenInfo(AuthenticationTokenInfo currentAuthenticationTokenInfo) {
        this.currentAuthenticationTokenInfo = currentAuthenticationTokenInfo;
        changeListener.forEach(authenticationChangeListener ->
            Platform.runLater(() -> authenticationChangeListener.changed(this.currentAuthenticationTokenInfo)
            ));
    }

    @Override
    public Optional<String> getCurrentAuthenticationToken() {
        return Optional.ofNullable(currentAuthenticationToken);
    }

    @Override
    public Optional<AuthenticationTokenInfo> getCurrentAuthenticationTokenInfo() {
        return Optional.ofNullable(currentAuthenticationTokenInfo);
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
