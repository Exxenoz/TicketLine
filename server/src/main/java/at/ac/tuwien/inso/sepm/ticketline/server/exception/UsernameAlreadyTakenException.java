package at.ac.tuwien.inso.sepm.ticketline.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Username already taken")
public class UsernameAlreadyTakenException extends RuntimeException {
    public UsernameAlreadyTakenException() {
    }

    public UsernameAlreadyTakenException(String message) {
        super(message);
    }
}
