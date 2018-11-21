package ar.edu.itba.pawgram.webapp.dto;

import ar.edu.itba.pawgram.model.User;

import java.net.URI;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class UserDTO {
    private long id;
    private String name;
    private String email;
    private URI url;

    @XmlElement(name = "profile_picture")
    private URI pictureURL;

    @XmlElement(name = "subscriptions")
    private URI subscriptionsURL;
    

    public UserDTO(){
    }

    public UserDTO(final User user, final URI baseUri) {
        id = user.getId();
        name = user.getName();
        email = user.getMail();
        url = baseUri.resolve("users/" + id);
        pictureURL = baseUri.resolve("users/images/" + user.getProfile_img_url());
        subscriptionsURL = baseUri.resolve("users/" + id + "/subscriptions");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public URI getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(URI pictureURL) {
        this.pictureURL = pictureURL;
    }

    public URI getSubscriptionsURL() {
        return subscriptionsURL;
    }

    public void setSubscriptionsURL(URI subscriptionsURL) {
        this.subscriptionsURL = subscriptionsURL;
    }
}
