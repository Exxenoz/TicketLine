package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;

import java.util.Optional;

public interface AuthenticationInformationService {

    /**
     * Set the current authentication token.
     *
     * @param currentAuthenticationToken representing the currently authenticated user authentication
     */
    void setCurrentAuthenticationToken(String currentAuthenticationToken);

    /**
     * Set the current authentication token info.
     *
     * @param currentAuthenticationTokenInfo representing the currently authenticated user
     */
    void setCurrentAuthenticationTokenInfo(AuthenticationTokenInfo currentAuthenticationTokenInfo);

    /**
     * Get the current authentication token.
     *
     * @return current authentication token
     */
    Optional<String> getCurrentAuthenticationToken();

    /**
     * Get the current authentication token info.
     *
     * @return current authentication token info
     */
    Optional<AuthenticationTokenInfo> getCurrentAuthenticationTokenInfo();

    /**
     * Clear the current authentication informations (logout).
     */
    void clearAuthentication();

    /**
     * Add a listener for authentication events (login/logout).
     *
     * @param listener for authentication change events.
     */
    void addAuthenticationChangeListener(AuthenticationChangeListener listener);

    /**
     * A listener which gets informed everytime an authentication info changes.
     * E.g.: on login/logout
     */
    @FunctionalInterface
    interface AuthenticationChangeListener {
        /**
         * Gets called whenever the current authentication info changes.
         * E.g.: on login/logout
         *
         * @param authenticationInfo representing the currently authenticated user
         */
        void changed(AuthenticationTokenInfo authenticationInfo);
    }

}
