package ar.edu.itba.pawgram.webapp.dto;

import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.PostImage;
import ar.edu.itba.pawgram.model.User;

import javax.xml.bind.annotation.XmlElement;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class PostDTO {
    private long id;
    private String title;
    private String description;
    private String contact_phone;
    private String category;
    private String pet;
    private boolean is_male;
    private UserDTO creator;
    private List<CommentDTO> comments;
    private LocationDTO locationDTO;
    private int distance;
    private URI url;
    private boolean subscribed;

    @XmlElement(name = "event_date")
    private Date event_date;

    @XmlElement(name = "image_urls")
    private PostImageListDTO imageURLs;

    /*@XmlElement(name = "subscribers_url")
    private URI subscribersURL;*/

    public PostDTO() {
    }

    public PostDTO(final Post post, final URI baseUri, final Optional<User> loggedUser){
        id = post.getId();
        title = post.getTitle();
        description = post.getDescription();
        contact_phone = post.getContact_phone();
        category = post.getCategory().getLowerName();
        pet = post.getPet().getLowerName();
        is_male = post.isIs_male();
        creator = new UserDTO(post.getOwner(),baseUri);
        comments = CommentDTO.fromCommentFamilyList(post.getCommentFamilies(),baseUri);
        locationDTO = new LocationDTO(post.getLocation(),baseUri);
        distance = post.getDistance();
        event_date = post.getEvent_date();
        setSubscribed(loggedUser.filter(post::isUserSubscribed).isPresent());

        url = baseUri.resolve("posts/"+id);
       // subscribersURL = baseUri.resolve("posts/"+id+"/subscriptions");

        imageURLs = new PostImageListDTO(post.getPostImages(),post.getPostImages().size(),baseUri);
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPet() {
        return pet;
    }

    public void setPet(String pet) {
        this.pet = pet;
    }

    public boolean isIs_male() {
        return is_male;
    }

    public void setIs_male(boolean is_male) {
        this.is_male = is_male;
    }

    public UserDTO getCreator() {
        return creator;
    }

    public void setCreator(UserDTO creator) {
        this.creator = creator;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public LocationDTO getLocationDTO() {
        return locationDTO;
    }

    public void setLocationDTO(LocationDTO locationDTO) {
        this.locationDTO = locationDTO;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public Date getEvent_date() {
        return event_date;
    }

    public void setEvent_date(Date event_date) {
        this.event_date = event_date;
    }

    public PostImageListDTO getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(PostImageListDTO imageURLs) {
        this.imageURLs = imageURLs;
    }

    /* public URI getSubscribersURL() {
        return subscribersURL;
    }

    public void setSubscribersURL(URI subscribersURL) {
        this.subscribersURL = subscribersURL;
    }*/
}
