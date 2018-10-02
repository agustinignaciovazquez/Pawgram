package ar.edu.itba.pawgram.webapp.form;

import javax.validation.constraints.Size;

public class MessageForm {
    @Size(min = 1, max = 1024)
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
