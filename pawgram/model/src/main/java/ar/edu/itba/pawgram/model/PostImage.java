package ar.edu.itba.pawgram.model;

import static org.apache.commons.lang3.Validate.isTrue;

public class PostImage extends Image {
    private long postId;

    public PostImage(long id, String url, long postId) {
        super(id, url);
        isTrue(postId >= 0, "postId ID must be non negative: %d", postId);
        this.postId = postId;
    }

    public long getPostId() {
        return postId;
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
        result = result * prime + (int)getId();

        return result;
    }

    @Override
    public String toString() {
        return "Image " + getUrl() + " from post " + postId;
    }
}
