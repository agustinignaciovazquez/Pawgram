package ar.edu.itba.pawgram.webapp.dto.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;

public class FormChangePassword {

    @NotEmpty
    @XmlElement(name = "current_password")
    private String currentPassword;

    @NotEmpty
    @Size(min = 6, max = 60)
    @XmlElement(name = "new_password")
    private String newPassword;

    public FormChangePassword() {}

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
}

