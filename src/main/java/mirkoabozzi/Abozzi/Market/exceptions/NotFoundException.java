package mirkoabozzi.Abozzi.Market.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID id) {
        super("Element with id " + id + " not found");
    }

    public NotFoundException(String msg) {
        super(msg);
    }
}
