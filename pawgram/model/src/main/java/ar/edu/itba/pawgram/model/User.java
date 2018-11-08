package ar.edu.itba.pawgram.model;

import javax.persistence.*;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notEmpty;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
	@SequenceGenerator(sequenceName = "users_id_seq", name = "users_id_seq", allocationSize = 1)
	@Column(name = "id")
	private long id;
	@Column(name = "name", length = 64, nullable = false)
	private String name;
	@Column(name = "surname", length = 64, nullable = false)
	private String surname;
	@Column(name = "mail", length = 256, nullable = false, unique = true)
	private String mail;
	@Column(name = "password", length = 60, nullable = false)
	private String password;
	@Column(name = "profile_img_url", length = 32, nullable = true)
	private String profile_img_url;

	// Hibernate
	User() {
	}

	// Dummy user constructor for testing
	User(long id, String name, String surname, String mail, String password, String profile_img_url) {
		super();
		isTrue(id >= 0, "User ID must be non negative: %d", id);

		this.id = id;
		this.mail = notBlank(mail,"User mail must have at least one non empty character");

		setName(name);
		setSurname(surname);
		setPassword(password);
		setProfile_img_url(profile_img_url);
	}

	public User(String name, String surname, String mail, String password, String profile_img_url) {
		this(0,name,surname,mail,password,profile_img_url);
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
	public String getProfile_img_url(){ return profile_img_url;}

	public void setPassword(String password) {
		this.password = notEmpty(password,"User password must have at least one character");
	}

	public void setProfile_img_url(String profile_img_url) {
		this.profile_img_url = profile_img_url;
	}

	public void setName(String name) {
		this.name = notBlank(name,"User name must have at least one non empty character");
	}

	public void setSurname(String surname) {
		this.surname = notBlank(surname,"User surname must have at least one non empty character");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof User))
			return false;
		
		User other = (User) obj;
		
		return (id == other.getId() || mail.equals(other.getMail()));
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
