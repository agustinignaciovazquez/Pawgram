package ar.edu.itba.pawgram.interfaces;

import ar.edu.itba.pawgram.model.*;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;

import java.time.LocalDateTime;
import java.util.List;

public interface PostDao {
    /**
     * Creates a {@link Post.PostBuilder} inserting the {@link Post} data into the database.
     * @param title - Title of the post
     * @param description - Description of the post
     * @param img_url - url img
     * @param contact_phone - contact phone of the owner
     * @param category - Category the post belongs to
     * @param event_date - Date of the event
     * @param pet - type of pet the post is for
     * @param is_male - true if the pet is male, false otherwise
     * @param location - location of the event
     * @param ownerId - owner id of the post
     * @return Post - The newly created post
     */
   /* public Post.PostBuilder createProduct(String name, String description, String shortDescription, String website, String category,
                                          LocalDateTime uploadDate, byte[] logo, int creatorId);*/
    public Post.PostBuilder createPost(final String title, final String description, final String img_url, final String contact_phone,
                                       final LocalDateTime event_date, final Category category, final Pet pet, final boolean is_male,
                                       final Location location, final long ownerId);

    /**
     * Lists every nearby in range KM existing {@link Post} as a {@link PlainPost} .
     * @param location - current location of the user
     * @param range - max range of search
     * @return {@link List} of the existing posts
     */
    public List<PlainPost> getPlainPosts(final Location location, final int range);

    /**
     * Lists every nearby in range KM existing {@link Post} as a {@link PlainPost} for a given {@link Category}
     * @param location - current location of the user
     * @param range - max range of search
     * @param category - Category the posts belongs to
     * @return {@link List} of the existing posts
     */
    public List<PlainPost> getPlainPostsByCategory(final Location location, final int range, final Category category);

    /**
     * Retrieves a {@link List} of {@link PlainPost} given a keyword ordered by the distance descendent
     * The keyword should match the post title or description
     * @param keyword - The keyword which should be matched
     * @param location - Current user location
     * @return The list of plain post that match with the keyword.
     */
    public List<PlainPost> getPlainPostsByKeyword(final String keyword, final Location location);

    /**
     * Lists post created by {@link User} as a {@link PlainPost} with the given userId.
     * @param userId - ID of the creator
     * @return List of post. Empty in case the user did not create any post
     */
    public List<PlainPost> getPlainPostsByUserId(final long userId);

    /**
     * Retrieves a {@link Post.PostBuilder} with every attribute set except for
     * the familyComments.
     * @param postId - ID of the post
     * @return Post with the associated ID of null if it doesn't exist
     */
    public Post.PostBuilder getFullPostById(final long postId);

    /**
     * Retrieves a {@link Post} as a {@link PlainPost}.
     * @param postId - ID of the post
     * @return Plain Post with the associated ID or null if it doesn't exist
     */
    public PlainPost getPlainProductById(final long postId);

    /**
     * Deletes a {@link Post} from the database.
     * @param postId - ID of the post to delete
     * @param user - user calling the method to check if has privileges
     * @return true if a product was deleted
     */
    public boolean deletePostById(final long postId, User user);

    /**
     * Retrieves the total amount of post registered.
     * @return The number of posts.
     */
    public long getTotalPosts();

    /**
     * Retrieves the total amount of post registered for a given {@link Category}
     * @param category - Category the posts belongs to
     * @return The number of posts.
     */
    public long getTotalPostsByCategory(final Category category);

    /**
     * Retrieves the total amount of post nearby.
     * @param location - current location of the user
     * @param range - max range of search
     * @return The number of posts.
     */
    public long getTotalPosts(final Location location,final int range);

    /**
     * Retrieves the total amount of post nearby for a given {@link Category}
     * @param location - current location of the user
     * @param range - max range of search
     * @param category - Category the posts belongs to
     * @return The number of posts.
     */
    public long getTotalPostsByCategory(final Location location,final int range,final Category category);
}
