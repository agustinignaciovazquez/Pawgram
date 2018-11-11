package ar.edu.itba.pawgram.interfaces.service;

import ar.edu.itba.pawgram.interfaces.exception.FileException;
import ar.edu.itba.pawgram.interfaces.exception.FileUploadException;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.PostImage;

import java.io.IOException;
import java.util.List;

public interface PostImageService {
    public static String UPLOAD_FOLDER = "D://temp//";

    /** LA USAMOS FUERA DE SERVICE??
     * Creates an {@link PostImage} for a {@link Post} saving the file
     * and inserting its name into the database.
     * @param post - ID of the post the image belongs to
     * @return The upload image name (randomly generated)

    public String createPostImage(final Post post, byte[] raw_image) throws FileUploadException;*/

    /**
     * Creates an {@link PostImage} for every item in {@link List} for a single {@link Post}
     * saving the file and inserting its name into the database.
     * @param post - ID of the post the image belongs to
     * @return The all the created PostImage's
     */
    public List<PostImage> createPostImage(final Post post, final List<byte[]> raw_images) throws FileUploadException;

    /**
     * Lists {@link PostImage} ID of a {@link Post}.
     * @param post - the post
     * @return {@link List} with the post images ID. Could be empty if the post possesses no images
     */
    public List<PostImage> getImagesIdByPostId(final Post post);

    /**
     * Get raw bytes of a image
     * @param filename - name of the image
     * @return raw bytes of image
     */
    public byte[] getImage(final String filename) throws FileException;

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
