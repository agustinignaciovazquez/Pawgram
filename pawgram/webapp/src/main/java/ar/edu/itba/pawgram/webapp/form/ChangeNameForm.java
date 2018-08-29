package ar.edu.itba.pawgram.webapp.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ChangeNameForm {
    @Size(min = 3, max = 50)
    @Pattern(regexp = "[a-zA-Z]+")
    private String name;
    @Size(min = 3, max = 50)
    @Pattern(regexp = "[a-zA-Z]+")
    private String surname;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
