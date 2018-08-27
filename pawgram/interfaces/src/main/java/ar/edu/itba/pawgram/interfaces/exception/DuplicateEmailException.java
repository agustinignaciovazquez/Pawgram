package ar.edu.itba.pawgram.interfaces.exception;

@SuppressWarnings("serial")
public class DuplicateEmailException extends Exception {

    public DuplicateEmailException() {
        super();
    }

    public DuplicateEmailException(String message) {
        super(message);
    }
}
