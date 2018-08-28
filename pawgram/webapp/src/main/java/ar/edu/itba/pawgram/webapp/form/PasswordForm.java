package ar.edu.itba.pawgram.webapp.form;

import ar.edu.itba.pawgram.webapp.form.constraints.FieldMatch;

import javax.validation.constraints.Size;

@FieldMatch(first = "password", second = "repeatPassword")
public class PasswordForm {

    @Size(min=6, max=60)
    private String password;
    @Size(min=6, max=60)
    private String repeatPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() { return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) { this.repeatPassword = repeatPassword;
    }
}
