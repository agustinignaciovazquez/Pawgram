package ar.edu.itba.pawgram.webapp.dto.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;

public class FormRecoverPassword {
    @Email
    @NotBlank
    private String mail;

    @NotEmpty
    @XmlElement(name = "token")
    private String token;

    @NotEmpty
    @Size(min = 6, max = 60)
    @XmlElement(name = "new_password")
    private String newPassword;

    public FormRecoverPassword() {
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
