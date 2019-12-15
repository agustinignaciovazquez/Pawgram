package ar.edu.itba.pawgram.webapp.dto;

import ar.edu.itba.pawgram.model.Notification;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.PostImage;
import ar.edu.itba.pawgram.model.User;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class PostImageDTO {
    private long postImageId;
    //private long postId;
    private URI url;


    public PostImageDTO() {
    }

    public PostImageDTO(final PostImage postImage, final URI baseUri) {
        this.postImageId = postImage.getPostImageId();
        //this.postId = postImage.getPostId();
        this.url = baseUri.resolve("posts/images/" + postImage.getUrl());
    }

    public long getPostImageId() {
        return postImageId;
    }

    public void setPostImageId(long postImageId) {
        this.postImageId = postImageId;
    }

    /*public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }*/

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }
}
