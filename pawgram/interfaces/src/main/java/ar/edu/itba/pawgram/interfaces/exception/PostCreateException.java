package ar.edu.itba.pawgram.interfaces.exception;


@SuppressWarnings("serial")
public class PostCreateException extends Exception {

    public PostCreateException() {
        super();
    }

    public PostCreateException(String message) {
        super(message);
    }
}
