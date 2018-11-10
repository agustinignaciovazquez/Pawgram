package ar.edu.itba.pawgram.interfaces.exception;

@SuppressWarnings("serial")
public class InvalidPostException extends Exception {

    public InvalidPostException() {
        super();
    }

    public InvalidPostException(String message) {
        super(message);
    }
}
