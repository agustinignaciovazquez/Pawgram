package ar.edu.itba.pawgram.model;

import ar.edu.itba.pawgram.model.interfaces.PlainPost;
import ar.edu.itba.pawgram.model.structures.Location;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.apache.commons.lang3.Validate.*;

@Entity
@Table(name = "posts")
public class Post implements PlainPost {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "posts_postid_seq")
	@SequenceGenerator(sequenceName = "posts_postid_seq", name = "posts_postid_seq", allocationSize = 1)
	@Column(name = "postid")
	private long id;
	@Column(name = "title", length = 64, nullable = false)
	private String title;
	@Column(name = "description", length = 2048, nullable = false)
	private String description;
	@Column(name = "contact_phone", length = 32, nullable = false)
	private String contact_phone;
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime event_date;
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
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "postId", orphanRemoval = true)
	@OrderBy("postImageId ASC")
	private List<PostImage> postImages;
	@Transient
	private int distance;
	@Transient
	private List<CommentFamily> commentFamilies;
	
	public static PostBuilder getBuilder(long id, String title, List<PostImage> postImages) {
		isTrue(id >= 0, "Post ID must be non negative: %d", id);
		notBlank(title, "Post title must contain at least one non blank character");
		return new PostBuilder(id, title, postImages);
	}

	public static PostBuilder getBuilderFromProduct(final Post post) {
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
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public Category getCategory() {
		return category;
	}

	@Override
	public List<PostImage> getPostImages() {
		return postImages;
	}

	@Override
	public Pet getPet() {
		return pet;
	}
	@Override
	public boolean isIs_male() {
		return is_male;
	}

	@Override
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
	public LocalDateTime getEvent_date() {
		return event_date;
	}


	public Location getLocation(){return location;}

	public User getOwner() { return owner; }


	public List<CommentFamily> getCommentFamilies() { return commentFamilies; }

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
		private LocalDateTime event_date;
		private Category category;
		private Pet pet;
		private boolean is_male;
		private Location location;
		private User owner;
		private int distance;
		private List<CommentFamily> commentFamilies = Collections.emptyList();
		private List<PostImage> postImages = Collections.emptyList();

		private PostBuilder(long id, String title, List<PostImage> postImages) {
			this.id = id;
			this.title = title;
			if(postImages != null)
				this.postImages = postImages;
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
			this.distance = post.distance;
		}

		public long getId(){
			return this.id;
		}

		public PostBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		public PostBuilder contact_phone(String contact_phone) {
			this.contact_phone = contact_phone;
			return this;
		}
		
		public PostBuilder event_date(LocalDateTime event_date) {
			this.event_date = event_date;
			return this;
		}
		
		public PostBuilder category(Category category) {
			this.category = category;
			return this;
		}
		
		public PostBuilder category(String category) {
			this.category = Category.valueOf(category.toUpperCase(Locale.ENGLISH).trim());
			return this;
		}

		public PostBuilder pet(Pet pet) {
			this.pet = pet;
			return this;
		}

		public PostBuilder pet(String pet) {
			this.pet = Pet.valueOf(pet.toUpperCase(Locale.ENGLISH).trim());
			return this;
		}

		public PostBuilder location(Location location) {
			this.location = location;
			return this;
		}

		public PostBuilder is_male(boolean is_male) {
			this.is_male = is_male;
			return this;
		}

		public PostBuilder user(User owner) {
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
			this.postImages = notNull(postImages, "post images  cannot be null");
			return this;
		}

		public Post build() {
			return new Post(this);
		}
	}
	
	
	
}
