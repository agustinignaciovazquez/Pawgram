package ar.edu.itba.pawgram.model;

import javax.persistence.*;

import java.io.Serializable;

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
    private long postId;
    @Column(length = 32, nullable = false)
    private final String url;

    // Hibernate
    /*PostImage() {
    }*/

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
        final int prime = 31;
        int result = 17;

        result = result * prime + (int)postId;
        result = result * prime + (int)postImageId;

        return result;
    }

    @Override
    public String toString() {
        return "Image " + getUrl() + " from post " + postId;
    }

    // For Hibernate Composite keys
    @SuppressWarnings("serial")
    public static class PostImagePrimaryKeyIds implements Serializable {
        private int postImageId;
        private int postId;

        public PostImagePrimaryKeyIds(int postImageId, int postId) {
            this.postImageId = postImageId;
            this.postId = postId;
        }

        // Hibernate
        public PostImagePrimaryKeyIds() {
        }

        public int getProductImageId() {
            return postImageId;
        }

        public int getProductId() {
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
            final int prime = 31;
            int result = 17;

            result = result * prime + getProductImageId();
            result = result * prime + getProductId();

            return result;
        }
    }
}
