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

public class PlainPostDTO {
    private long id;
    private String title;
    private String description;
    private String category;
    private String pet;
    private boolean is_male;
    private LocationDTO locationDTO;
    private int distance;
    private URI url;

    @XmlElement(name = "creator_id")
    private long creatorId;

    @XmlElement(name = "creator_url")
    private URI creatorURL;

    @XmlElement(name = "event_date")
    private Date event_date;

    @XmlElement(name = "image_urls")
    private List<URI> imageURLs;

    public PlainPostDTO() {
    }

    public PlainPostDTO(final Post post, final URI baseUri, final Optional<User> loggedUser){
        id = post.getId();
        title = post.getTitle();
        description = post.getDescription();
        category = post.getCategory().getLowerName();
        pet = post.getPet().getLowerName();
        is_male = post.isIs_male();
        locationDTO = new LocationDTO(post.getLocation(),baseUri);
        distance = post.getDistance();
        event_date = post.getEvent_date();
        creatorId = post.getOwner().getId();

        url = baseUri.resolve("posts/"+id);
        creatorURL = baseUri.resolve("users/" + creatorId);

        imageURLs = new ArrayList<>();
        for (final PostImage pi : post.getPostImages())
            imageURLs.add(baseUri.resolve("posts/images/" + pi.getUrl()));
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public URI getCreatorURL() {
        return creatorURL;
    }

    public void setCreatorURL(URI creatorURL) {
        this.creatorURL = creatorURL;
    }

    public Date getEvent_date() {
        return event_date;
    }

    public void setEvent_date(Date event_date) {
        this.event_date = event_date;
    }

    public List<URI> getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(List<URI> imageURLs) {
        this.imageURLs = imageURLs;
    }
}
