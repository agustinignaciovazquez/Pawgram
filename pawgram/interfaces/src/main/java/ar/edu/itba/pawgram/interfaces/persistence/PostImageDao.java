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
     * Lists {@link PostImage} of a {@link Post}.
     * @param postId - ID of the post
     * @return {@link List} with the post images ID. Could be empty if the post possesses no images
     */
    public List<PostImage> getImagesIdByPostId(final long postId);

    /**
     * Counts total of {@link PostImage} of a {@link Post}.
     * @param postId - ID of the post
     * @return {@link List} with the post images ID. 0 if the post possesses no images
     */
    public long getTotalImagesByPostId(final long postId);

    /**
     * Gets {@link PostImage} from the DB
     * @param postId - ID of the post
     * @param postImageId - ID of the postImage
     * @return {@link List} with the post images ID. 0 if the post possesses no images
     */
    public PostImage getPostImageById(final long postId, final long postImageId);

    /**
     * Deletes {@link PostImage} of a {@link Post} from DB.
     * @param postId - ID of the post
     * @param postImageId - ID of the postImage
     * @return {@link List} with the post images ID. 0 if the post possesses no images
     */
    public boolean deletePostImage(final long postId, final long postImageId);

}
