package at.ac.tuwien.inso.sepm.ticketline.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.LOCKED, reason = "Locked")
public class HttpLockedException extends RuntimeException {
    public HttpLockedException() {
    }

    public HttpLockedException(String message) {
        super(message);
    }
}
