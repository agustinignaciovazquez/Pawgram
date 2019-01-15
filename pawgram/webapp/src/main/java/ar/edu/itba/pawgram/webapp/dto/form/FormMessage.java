package ar.edu.itba.pawgram.webapp.dto.form;

import javax.validation.constraints.Size;

public class FormMessage {
    @Size(min = 1, max = 1024)
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
