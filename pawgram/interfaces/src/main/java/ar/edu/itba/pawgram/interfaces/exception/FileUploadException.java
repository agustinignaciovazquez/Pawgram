package ar.edu.itba.pawgram.interfaces.exception;

@SuppressWarnings("serial")
public class FileUploadException extends Exception {

    public FileUploadException() {
        super();
    }

    public FileUploadException(String message) {
        super(message);
    }
}
