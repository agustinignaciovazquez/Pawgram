package ar.edu.itba.pawgram.webapp.dto;

import ar.edu.itba.pawgram.model.Notification;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.PostImage;
import ar.edu.itba.pawgram.model.User;

import javax.xml.bind.annotation.XmlElement;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class NotificationDTO {
    private long id;
    private PlainPostDTO post;
    private CommentDTO comment;
    private boolean is_seen;

    @XmlElement(name = "date")
    private Date date;

    public NotificationDTO() {
    }

    public NotificationDTO(final Notification notification, final URI baseUri, final Optional<User> loggedUser){
        this.id = notification.getId();
        this.is_seen = notification.isIs_seen();

        if(notification.getPost() != null)
            this.post = new PlainPostDTO(notification.getPost(),baseUri,loggedUser);

        if(notification.getComment() != null)
            this.comment = new CommentDTO(notification.getComment(),baseUri);

        //we might need this later url = baseUri.resolve("posts/"+id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PlainPostDTO getPost() {
        return post;
    }

    public void setPost(PlainPostDTO post) {
        this.post = post;
    }

    public CommentDTO getComment() {
        return comment;
    }

    public void setComment(CommentDTO comment) {
        this.comment = comment;
    }

    public boolean isIs_seen() {
        return is_seen;
    }

    public void setIs_seen(boolean is_seen) {
        this.is_seen = is_seen;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
