package ar.edu.itba.pawgram.webapp.form;

import ar.edu.itba.pawgram.webapp.form.constraints.FieldMatch;

import javax.validation.constraints.Size;

@FieldMatch(first = "password", second = "passwordConf")
public class FormPassword {

    @Size(min=6, max=60)
    private String password;

    private String passwordConf;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConf() {
        return passwordConf;
    }

    public void setPasswordConf(String passwordConf) {
        this.passwordConf = passwordConf;
    }
}
