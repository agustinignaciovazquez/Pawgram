package ar.edu.itba.pawgram.webapp.form;

import javax.validation.Valid;

public class ChangePasswordForm {
    private String currentPassword;

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

    public PasswordForm getPasswordForm() {
        return passwordForm;
    }

    public void setPasswordForm(PasswordForm passwordForm) {
        this.passwordForm = passwordForm;
    }
}
