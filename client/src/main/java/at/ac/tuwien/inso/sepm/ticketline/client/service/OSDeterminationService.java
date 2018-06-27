package at.ac.tuwien.inso.sepm.ticketline.client.service;

public interface OSDeterminationService {

    /**
     * Checks whether the current OS the client is running is Windows
     * @return true if is running on Windows, else false
     */
    boolean isWindows();

    /**
     * Checks whether the current OS the client is running on is Mac OS
     * @return true if is running on Mac, else false
     */
    boolean isMac();

    /**
     * Checks whether the curren OS the client is running on is Linux/Unix-based
     * @return true if is running on Linux/Unix, else false
     */
    boolean isUnix();
}
