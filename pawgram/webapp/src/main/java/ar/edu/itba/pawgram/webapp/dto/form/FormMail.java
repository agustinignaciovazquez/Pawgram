package ar.edu.itba.pawgram.webapp.dto.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class FormMail {
    @Email
    @NotBlank
    private String mail;

    public FormMail() {
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
