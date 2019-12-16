package ar.edu.itba.pawgram.webapp.dto;

import ar.edu.itba.pawgram.model.User;

import java.net.URI;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class UserDTO {
    private long id;
    private String name;
    private String surname;
    private String email;
    private URI url;

    @XmlElement(name = "profile_picture")
    private URI pictureURL;

    @XmlElement(name = "user_posts")
    private URI userPostsURL;
    

    public UserDTO(){
    }

    public UserDTO(final User user, final URI baseUri) {
        id = user.getId();
        name = user.getName();
        surname = user.getSurname();
        email = user.getMail();
        url = baseUri.resolve("users/" + id);
        pictureURL = user.getProfile_img_url() == null? null : baseUri.resolve("users/images/" + user.getProfile_img_url());
        userPostsURL = baseUri.resolve("users/" + id + "/posts");
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public URI getUserPostsURL() {
        return userPostsURL;
    }

    public void setUserPostsURL(URI userPostsURL) {
        this.userPostsURL = userPostsURL;
    }
}
