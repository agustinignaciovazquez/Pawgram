package ar.edu.itba.pawgram.interfaces.exception;

@SuppressWarnings("serial")
public class MaxSearchZoneReachedException extends Exception {

    public MaxSearchZoneReachedException() {
        super();
    }

    public MaxSearchZoneReachedException(String message) {
        super(message);
    }
}