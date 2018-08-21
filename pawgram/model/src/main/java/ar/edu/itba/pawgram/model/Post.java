package ar.edu.itba.pawgram.model;

import ar.edu.itba.pawgram.model.interfaces.PlainPost;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;

public class Post implements PlainPost {
	private long id;
	private String title;
	private String description;
	private String img_url;
	private String contact_phone;
	private LocalDateTime event_date;
	private Category category;
	private Pet pet;
	private boolean is_male;
	private Location location;
	private User owner;
	private int distance;
	private List<CommentFamily> comments;
	
	public static PostBuilder getBuilder(long id, String title, String img_url) {
		isTrue(id >= 0, "Post ID must be non negative: %d", id);
		notBlank(title, "Post title must contain at least one non blank character");
		notBlank(title, "Post img url must contain at least one non blank character");
		
		return new PostBuilder(id, title, img_url);
	}
	
	private Post(PostBuilder builder) {
		this.id = builder.id;
		this.title = builder.title;
		this.description = builder.description;
		this.img_url = builder.img_url;
		this.contact_phone = builder.contact_phone;
		this.event_date = builder.event_date;
		this.category = builder.category;
		this.pet = builder.pet;
		this.is_male = builder.is_male;
		this.location = builder.location;
		this.owner = builder.owner;
		this.comments = builder.comments;
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
	public String getDescription() {
		return description;
	}
	@Override
	public String getImg_url() {
		return img_url;
	}
	public String getContact_phone() {
		return contact_phone;
	}
	public LocalDateTime getevent_date() {
		return event_date;
	}
	@Override
	public Category getCategory() {
		return category;
	}
	@Override
	public Pet getPet() {
		return pet;
	}
	public boolean isIs_male() {
		return is_male;
	}
	public Location getLocation(){return location;}
	public User getOwner() {
		return owner;
	}
	
	public static class PostBuilder{
		private long id;
		private String title;
		private String description;
		private String img_url;
		private String contact_phone;
		private LocalDateTime event_date;
		private Category category;
		private Pet pet;
		private boolean is_male;
		private Location location;
		private User owner;
		private int distance;
		private List<CommentFamily> comments = Collections.emptyList();

		private PostBuilder(long id, String title, String img_url) {
			this.id = id;
			this.title = title;
			this.img_url = img_url;
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
		
		public Post build() {
			return new Post(this);
		}
	}
	
	
	
}
