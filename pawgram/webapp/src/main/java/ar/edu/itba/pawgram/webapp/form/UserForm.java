package ar.edu.itba.pawgram.webapp.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class UserForm {
	@Size(min = 3, max = 50)
	@Pattern(regexp = "[a-zA-Z]+")
	private String name;
	@Size(min = 3, max = 50)
	@Pattern(regexp = "[a-zA-Z]+")
	private String surname;
	@NotNull
    @NotEmpty
    @Email
	private String mail;
	@Size(min = 8, max = 100)
	private String password;
	@Size(min = 8, max = 100)
	private String repeatPassword;
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
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRepeatPassword() {
		return repeatPassword;
	}
	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}
	
}
