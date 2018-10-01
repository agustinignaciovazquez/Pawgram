package ar.edu.itba.pawgram.interfaces.exception;

@SuppressWarnings("serial")
public class SendMailException extends Exception {

    public SendMailException() {
        super();
    }

    public SendMailException(String message) {
        super(message);
    }
}
