package ar.edu.itba.pawgram.model;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notEmpty;

public class User {
	private long id;
	private String name;
	private String surname;
	private String mail;
	private String password;
	private String profile_img_url;

	public User(long id, String name, String surname, String mail, String password) {
		super();
		isTrue(id >= 0, "User ID must be non negative: %d", id);
		this.id = id;
		this.name = notBlank(name,"User name must have at least one non empty character");
		this.surname = notBlank(surname,"User surname must have at least one non empty character");
		this.mail = notBlank(mail,"User mail must have at least one non empty character");
		this.password = notEmpty(password,"User password must have at least one character");
	}
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getSurname() {
		return surname;
	}
	public String getMail() {
		return mail;
	}
	public String getPassword() {
		return password;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof User))
			return false;
		
		User other = (User) obj;
		
		return id == other.getId() || mail.equals(other.getMail());
	}
	
	@Override
	public int hashCode() {
		return (int)id;
	}
	
	@Override
	public String toString() {
		return name + " " + surname;
	}
}
