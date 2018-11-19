package ar.edu.itba.pawgram.model;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

@Entity
@IdClass(PostImage.PostImagePrimaryKeyIds.class)
@Table(name = "postimages")
public class PostImage  {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "postsimages_postimageid_seq")
    @SequenceGenerator(name = "postsimages_postimageid_seq", sequenceName = "postsimages_postimageid_seq", allocationSize = 1)
    @Column(name = "postimageid")
    private long postImageId;

    @Id
    @Column(name = "postid")
    private long postId;

    @Column(name = "url",length = 32, nullable = false)
    private String url;

    // Hibernate
    PostImage() {
    }

    public PostImage(long id, String url, long postId) {
        isTrue(id >= 0, "Image ID must be non negative: %d", id);
        isTrue(postId >= 0, "Post ID must be non negative: %d", postId);
        this.postId = postId;
        this.postImageId = id;
        this.url = url;
    }

    public PostImage(String url, long postId){
        this(0,url,postId);
    }

    public long getPostImageId() {
        return postImageId;
    }

    public long getPostId() {
        return postId;
    }

    public long getId() {
        return postImageId;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof PostImage))
            return false;

        PostImage other = (PostImage) obj;

        return getId() == other.getId() && postId == other.getPostId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(postImageId,postId);
    }

    @Override
    public String toString() {
        return "Image " + getUrl() + " from post " + postId;
    }

    // For Hibernate Composite keys
    @SuppressWarnings("serial")
    public static class PostImagePrimaryKeyIds implements Serializable {
        private long postImageId;
        private long postId;

        public PostImagePrimaryKeyIds(long postImageId, long postId) {
            this.postImageId = postImageId;
            this.postId = postId;
        }

        // Hibernate
        public PostImagePrimaryKeyIds() {
        }

        public long getPostImageId() {
            return postImageId;
        }

        public long getPostId() {
            return postId;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (!(obj instanceof PostImagePrimaryKeyIds))
                return false;

            final PostImagePrimaryKeyIds other = (PostImagePrimaryKeyIds) obj;

            return postId == other.postId && postImageId == other.postImageId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(postImageId,postId);
        }
    }
}
