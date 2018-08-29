package ar.edu.itba.pawgram.webapp.form;

import javax.validation.Valid;

public class ChangePasswordForm {
    private String currentPassword;
    private String currentPasswordConf;

    @Valid
    private PasswordForm passwordForm;

    public ChangePasswordForm() {
        this.passwordForm = new PasswordForm();
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getCurrentPasswordConf() {
        return currentPasswordConf;
    }

    public void setCurrentPasswordConf(String currentPasswordConf) {
        this.currentPasswordConf = currentPasswordConf;
    }

    public PasswordForm getPasswordForm() {
        return passwordForm;
    }
}
