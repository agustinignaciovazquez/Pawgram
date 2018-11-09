package ar.edu.itba.pawgram.interfaces.exception;

@SuppressWarnings("serial")
public class InvalidSearchZoneException extends Exception {

    public InvalidSearchZoneException() {
        super();
    }

    public InvalidSearchZoneException(String message) {
        super(message);
    }
}
