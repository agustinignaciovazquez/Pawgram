package ar.edu.itba.pawgram.webapp.dto;

import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement
public class PostListDTO {
    private List<PlainPostDTO> posts;
    private long totalCount;
    private long count;

    public PostListDTO() {
    }

    public PostListDTO(final List<Post> posts, long totalCount, final URI baseUri, final Optional<User> loggedUser) {
        this.posts = new LinkedList<>();
        for (final Post p : posts) {
            this.posts.add(new PlainPostDTO(p, baseUri, loggedUser));
        }
        this.totalCount = totalCount;
        this.count = posts.size();
    }

    public List<PlainPostDTO> getposts() {
        return posts;
    }

    public void setposts(List<PlainPostDTO> posts) {
        this.posts = posts;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
