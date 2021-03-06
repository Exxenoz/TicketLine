package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.service.OSDeterminationService;
import org.springframework.stereotype.Service;

@Service
public class SimpleOSDeterminationService implements OSDeterminationService {

    private static String OS = System.getProperty("os.name").toLowerCase();

    @Override
    public boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    @Override
    public boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    @Override
    public boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }
}
