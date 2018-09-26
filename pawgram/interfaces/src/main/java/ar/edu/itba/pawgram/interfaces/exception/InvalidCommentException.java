package ar.edu.itba.pawgram.interfaces.exception;

@SuppressWarnings("serial")
public class InvalidCommentException extends Exception {

    public InvalidCommentException() {
        super();
    }

    public InvalidCommentException(String message) {
        super(message);
    }
}
