package ar.edu.itba.pawgram.model;

import ar.edu.itba.pawgram.model.comparator.PostDateComparator;
import org.hibernate.annotations.SortComparator;

import javax.persistence.*;

import java.util.Collections;
import java.util.SortedSet;

import static org.apache.commons.lang3.Validate.*;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
	@SequenceGenerator(sequenceName = "users_id_seq", name = "users_id_seq", allocationSize = 1)
	@Column(name = "id")
	@Access(AccessType.PROPERTY)
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

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(	name = "subscriptions", joinColumns = @JoinColumn(name = "userId", nullable = false, foreignKey = @ForeignKey(name = "user_id_constraint")),
			inverseJoinColumns = @JoinColumn(name = "postId", nullable = false, foreignKey = @ForeignKey(name = "post_id_constraint")))
	@SortComparator(PostDateComparator.class)
	private SortedSet<Post> postSubscriptions;

	// Hibernate
	User() {
	}

	// Dummy user constructor for testing
	private User(long id, String name, String surname, String mail, String password, String profile_img_url) {
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

	public void setId(long id) {
		this.id = id;
	}

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

	public SortedSet<Post> getPostSubscriptions() {
		return Collections.unmodifiableSortedSet(postSubscriptions);
	}

	public boolean subscribePost(final Post post) {
		return postSubscriptions.add(notNull(post, "Post to suscribe by user " + this + "cannot be null"))
				&& post.addSubscription(this);
	}

	public boolean unSubscribePost(final Post post) {
		return postSubscriptions.remove(notNull(post, "Post to unsuscribe by user " + this + " cannot be null"))
				&& post.removeSubscription(this);
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
