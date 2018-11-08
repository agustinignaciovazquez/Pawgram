package ar.edu.itba.pawgram.interfaces.persistence;

import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.PostImage;

import java.util.List;

public interface PostImageDao {
    /**
     * Creates an {@link PostImage} for a {@link Post} inserting it into the database.
     * @param postId - id of post the image belongs to
     * @return The created PostImage
     */
    public PostImage createPostImage(final long postId, String url);

    /**
     * Lists {@link PostImage} ID of a {@link Post}.
     * @param postId - ID of the post
     * @return {@link List} with the post images ID. Could be empty if the post possesses no images
     */
    public List<PostImage> getImagesIdByPostId(final long postId);

}
