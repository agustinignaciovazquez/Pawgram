package ar.edu.itba.pawgram.interfaces.exception;

@SuppressWarnings("serial")
public class InvalidNotificationException extends Exception {

    public InvalidNotificationException() {
        super();
    }

    public InvalidNotificationException(String message) {
        super(message);
    }
}