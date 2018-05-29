package at.ac.tuwien.inso.sepm.ticketline.server.exception.endpoint;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class HttpForbiddenException extends RuntimeException {
}
