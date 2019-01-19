package ar.edu.itba.pawgram.interfaces.exception;

@SuppressWarnings("serial")
public class InvalidQueryException extends Exception {

    public InvalidQueryException() {
        super();
    }

    public InvalidQueryException(String message) {
        super(message);
    }
}
