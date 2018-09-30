package ar.edu.itba.pawgram.interfaces.exception;

@SuppressWarnings("serial")
public class FileException extends Exception {

    public FileException() {
        super();
    }

    public FileException(String message) {
        super(message);
    }
}
