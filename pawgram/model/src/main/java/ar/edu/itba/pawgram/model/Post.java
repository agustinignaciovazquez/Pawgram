package ar.edu.itba.pawgram.model;

import ar.edu.itba.pawgram.model.comparator.UserAlphaComparator;
import ar.edu.itba.pawgram.model.structures.Location;
import org.hibernate.annotations.SortComparator;

import javax.persistence.*;
import java.util.*;

import static org.apache.commons.lang3.Validate.*;

@Entity
@Table(name = "posts")
@NamedStoredProcedureQuery(
		name="haversine_distance",
		procedureName="haversine_distance",
		resultClasses = {
				Double.class
		},
		parameters={
				@StoredProcedureParameter(name="lat1", type=Double.class, mode=ParameterMode.IN),
				@StoredProcedureParameter(name="lon1", type=Double.class, mode=ParameterMode.IN),
				@StoredProcedureParameter(name="lat2", type=Double.class, mode=ParameterMode.IN),
				@StoredProcedureParameter(name="lon2", type=Double.class, mode=ParameterMode.IN)
		}
)

public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "posts_postid_seq")
	@SequenceGenerator(sequenceName = "posts_postid_seq", name = "posts_postid_seq", allocationSize = 1)
	@Column(name = "postid")
	@Access(AccessType.PROPERTY)
	private long id;

	@Column(name = "title", length = 64, nullable = false)
	private String title;

	@Column(name = "description", length = 2048, nullable = false)
	private String description;

	@Column(name = "contact_phone", length = 32, nullable = false)
	private String contact_phone;

	@Temporal(TemporalType.TIMESTAMP)
	private Date event_date;

	@Enumerated(EnumType.STRING)
	private Category category;

	@Enumerated(EnumType.STRING)
	private Pet pet;

	@Column(name = "is_male",nullable = false)
	private boolean is_male;

	@Embedded
	private Location location;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "userid", nullable = false, updatable = false)
	private User owner;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "postId", orphanRemoval = true)
	@OrderBy("postImageId ASC")
	private List<PostImage> postImages;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "postSubscriptions")
	@SortComparator(UserAlphaComparator.class)
	private SortedSet<User> userSubscriptions;

	@Transient
	private int distance;

	@Transient
	private List<CommentFamily> commentFamilies = Collections.emptyList();
	
	public static PostBuilder getBuilder(String title, List<PostImage> postImages) {
		notBlank(title, "Post title must contain at least one non blank character");
		notNull(postImages, "post images cannot be null");
		return new PostBuilder(title, postImages);
	}

	public static PostBuilder getBuilderFromPost(final Post post) {
		notNull(post, "Post cannot be null in order to retrieve Builder");
		return new PostBuilder(post);
	}

	// Hibernate
	Post() {
	}

	private Post(PostBuilder builder) {
		this.id = builder.id;
		this.title = builder.title;
		this.description = builder.description;
		this.postImages = builder.postImages;
		this.contact_phone = builder.contact_phone;
		this.event_date = builder.event_date;
		this.category = builder.category;
		this.pet = builder.pet;
		this.is_male = builder.is_male;
		this.location = builder.location;
		this.owner = builder.owner;
		this.commentFamilies = builder.commentFamilies;
		this.distance = builder.distance;
		this.userSubscriptions = builder.userSubscriptions;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}


	public Category getCategory() {
		return category;
	}

	public Pet getPet() {
		return pet;
	}

	public boolean isIs_male() {
		return is_male;
	}

	public int getDistance() {
		//TO RETURN DISTANCE IN KM!
		 return distance/1000;
	}

	public String getDescription() {
		return description;
	}

	public String getContact_phone() {
		return contact_phone;
	}

	public Date getEvent_date() {
		return event_date;
	}

	public Location getLocation(){return location;}

	public User getOwner() { return owner; }

	public List<PostImage> getPostImages() {
		return Collections.unmodifiableList(postImages);
	}

	public List<CommentFamily> getCommentFamilies() { return Collections.unmodifiableList(commentFamilies); }

	public SortedSet<User> getUserSubscriptions() {
		return Collections.unmodifiableSortedSet(userSubscriptions);
	}

	public int getTotalSubscriptions(){
		return userSubscriptions.size();
	}

	public boolean addSubscription(final User user) {
		return userSubscriptions.add(notNull(user, "Subscription to add to post " + this + " cannot be null"));
	}

	public boolean removeSubscription(final User user) {
		return userSubscriptions.remove(notNull(user, "Subscription to remove from post " + this + " cannot be null"));
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Post))
			return false;

		Post other = (Post) obj;

		return id == other.getId();
	}

	@Override
	public int hashCode() {
		return (int)id;
	}

	@Override
	public String toString() {
		return title;
	}

	public static class PostBuilder{
		private long id;
		private String title;
		private String description;
		private String contact_phone;
		private Date event_date;
		private Category category;
		private Pet pet;
		private boolean is_male;
		private Location location;
		private User owner;
		private int distance;
		private List<CommentFamily> commentFamilies = Collections.emptyList();
		private List<PostImage> postImages = Collections.emptyList();
		private SortedSet<User> userSubscriptions = new TreeSet<>(new UserAlphaComparator()); // mutable

		private PostBuilder(String title, List<PostImage> postImages) {
			this.title = title;
			this.postImages = notNull(postImages, "post images  cannot be null");
		}

		private PostBuilder(Post post) {
			this.id = post.id;
			this.title = post.title;
			this.description = post.description;
			this.postImages = post.postImages;
			this.contact_phone = post.contact_phone;
			this.event_date = post.event_date;
			this.category = post.category;
			this.pet = post.pet;
			this.is_male = post.is_male;
			this.location = post.location;
			this.owner = post.owner;
			this.commentFamilies = post.commentFamilies;
			this.userSubscriptions = post.userSubscriptions;
			this.distance = post.distance;
		}

		public long getId(){
			return this.id;
		}

		public PostBuilder id(long id) {
			isTrue(id >= 0, "Post ID must be non negative: %d", id);
			this.id = id;
			return this;
		}

		public PostBuilder title(String title) {
			if(title != null)
				this.title = notBlank(title, "Post title must contain at least one non blank character");
			return this;
		}

		public PostBuilder description(String description) {
			if(description != null)
				this.description = notBlank(description, "Post description must contain at least one non blank character");
			return this;
		}
		
		public PostBuilder contact_phone(String contact_phone) {
			if(contact_phone != null)
				this.contact_phone = notBlank(contact_phone, "Post contact phone must contain at least one non blank character");
			return this;
		}
		
		public PostBuilder event_date(Date event_date) {
			if(event_date != null)
				this.event_date = event_date;
			return this;
		}
		
		public PostBuilder category(Category category) {
			if(category != null)
				this.category = category;
			return this;
		}
		
		public PostBuilder category(String category) {
			if(category != null)
				this.category = Category.valueOf(category.toUpperCase(Locale.ENGLISH).trim());
			return this;
		}

		public PostBuilder pet(Pet pet) {
			if(pet != null)
				this.pet = pet;
			return this;
		}

		public PostBuilder pet(String pet) {
			if(pet != null)
				this.pet = Pet.valueOf(pet.toUpperCase(Locale.ENGLISH).trim());
			return this;
		}

		public PostBuilder location(Location location) {
			if(location != null)
				this.location = location;
			return this;
		}

		public PostBuilder is_male(Boolean is_male) {
			if(is_male != null)
				this.is_male = is_male;
			return this;
		}

		public PostBuilder user(User owner) {
			if(owner != null)
				this.owner = owner;
			return this;
		}

		public PostBuilder distance(int distance){
			this.distance = distance;
			return this;
		}
		public PostBuilder commentFamilies(List<CommentFamily> commentFamilies) {
			this.commentFamilies = notNull(commentFamilies, "post comment families cannot be null");
			return this;
		}

		public PostBuilder postImages(List<PostImage> postImages) {
			this.postImages = notNull(postImages, "post images cannot be null");
			return this;
		}

		public PostBuilder userSubscriptions(final SortedSet<User> userSubscriptions) {
			this.userSubscriptions = notNull(userSubscriptions, "Subscription users set cannot be null");
			return this;
		}

		public Post build() {
			return new Post(this);
		}
	}
	
	
	
}
