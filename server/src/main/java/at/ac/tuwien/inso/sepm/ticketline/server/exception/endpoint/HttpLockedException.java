package at.ac.tuwien.inso.sepm.ticketline.server.exception.endpoint;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.LOCKED)
public class HttpLockedException extends RuntimeException {
    public HttpLockedException() {
    }

    public HttpLockedException(String message) {
        super(message);
    }
}
