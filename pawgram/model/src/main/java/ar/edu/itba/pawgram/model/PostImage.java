package ar.edu.itba.pawgram.model;

import javax.persistence.*;

import static org.apache.commons.lang3.Validate.notNull;

@Entity
@Table(name = "postimages")
@SequenceGenerator(name = "postsimages_postimageid_seq", sequenceName = "postsimages_postimageid_seq", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "postImageId"))
public class PostImage extends Image {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "postid", nullable = false, updatable = false)
    private Post post;

    // Hibernate
    /*PostImage() {
    }*/

    public PostImage(long id, String url, Post post) {
        super(id, url);
        this.post = notNull(post,"postId ID must be non negative: %d");
    }

    public PostImage(String url, Post post){
        this(0,url,post);
    }

    public Post getPost() {
        return post;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "postsimages_postimageid_seq")
    public long getId(){
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof PostImage))
            return false;

        PostImage other = (PostImage) obj;

        return getId() == other.getId() && post.getId() == other.post.getId();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 17;

        result = result * prime + (int)post.getId();
        result = result * prime + (int)getId();

        return result;
    }

    @Override
    public String toString() {
        return "Image " + getUrl() + " from post " + post.getId();
    }
}
