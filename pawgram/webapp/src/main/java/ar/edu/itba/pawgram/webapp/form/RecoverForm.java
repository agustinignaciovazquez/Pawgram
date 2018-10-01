package ar.edu.itba.pawgram.webapp.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;

public class RecoverForm {
    private String currentToken;
    @Email
    @NotBlank
    private String mail;
    @Valid
    private PasswordForm passwordForm;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public RecoverForm() {
        this.passwordForm = new PasswordForm();
    }

    public String getCurrentToken() {
        return currentToken;
    }

    public void setCurrentToken(String currentToken) {
        this.currentToken = currentToken;
    }

    public PasswordForm getPasswordForm() {
        return passwordForm;
    }
}
