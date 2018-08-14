package ar.edu.itba.pawgram.model;

public class User {
	private long id;
	private String name;
	private String surname;
	private String mail;
	private String password;
	public User(long id, String name, String surname, String mail, String password) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.mail = mail;
		this.password = password;
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
	
}
