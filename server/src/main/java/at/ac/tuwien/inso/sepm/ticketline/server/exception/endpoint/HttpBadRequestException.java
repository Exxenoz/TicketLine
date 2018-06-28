package at.ac.tuwien.inso.sepm.ticketline.server.exception.endpoint;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Request was invalid")
public class HttpBadRequestException extends RuntimeException {

    public HttpBadRequestException() {
        super();
    }

    public HttpBadRequestException(String message) {
        super(message);
    }
}
