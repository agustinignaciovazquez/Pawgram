package ar.edu.itba.pawgram.interfaces.service;

import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.PostImage;

import java.io.IOException;
import java.util.List;

public interface PostImageService {
    public static String UPLOAD_FOLDER = "/Users/agustinvazquez/Downloads/"; /*tiene que terminar en / */

    /**
     * Creates an {@link PostImage} for a {@link Post} saving the file
     * and inserting its name into the database.
     * @param postId - ID of the post the image belongs to
     * @return The upload image name (randomly generated)
     */
    public String createPostImage(final long postId, byte[] raw_image) throws IOException;

    /**
     * Creates an {@link PostImage} for every item in {@link List} for a single {@link Post}
     * saving the file and inserting its name into the database.
     * @param postId - ID of the post the image belongs to
     * @return The all the created PostImage's
     */
    public List<PostImage> createPostImage(final long postId, final List<byte[]> raw_images) throws IOException;

    /**
     * Lists {@link PostImage} ID of a {@link Post}.
     * @param postId - ID of the post
     * @return {@link List} with the post images ID. Could be empty if the post possesses no images
     */
    public List<PostImage> getImagesIdByPostId(final long postId);

    /**
     * Get raw bytes of a image
     * @param filename - name of the image
     * @return raw bytes of image
     */
    public byte[] getImage(final String filename) throws IOException;
}
