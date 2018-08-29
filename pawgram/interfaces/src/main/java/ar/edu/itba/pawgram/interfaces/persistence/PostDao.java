package ar.edu.itba.pawgram.interfaces.persistence;

import ar.edu.itba.pawgram.interfaces.exception.PostCreateException;
import ar.edu.itba.pawgram.model.*;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface PostDao {
    /**
     * Creates a {@link Post.PostBuilder} inserting the {@link Post} data into the database.
     * @param title - Title of the post
     * @param description - Description of the post
     * @param raw_images - raw img's
     * @param contact_phone - contact phone of the owner
     * @param category - Category the post belongs to
     * @param event_date - Date of the event
     * @param pet - type of pet the post is for
     * @param is_male - true if the pet is male, false otherwise
     * @param location - location of the event
     * @param owner - owner id of the post
     * @return Post - The newly created post
     */
    public Post.PostBuilder createPost(String title, String description, List<byte[]> raw_images, String contact_phone, LocalDateTime event_date,
                                       Category category, Pet pet, boolean is_male, Location location, User owner) throws PostCreateException;

    /**
     * Lists every post {@link PlainPost}
     * @param limit - max number of results
     * @param offset - post offset
     * @return {@link List} of the existing posts (distance as 0)
     */
    public List<PlainPost> getPlainPostsRange(final int limit, final long offset);

    /**
     * Lists every post {@link PlainPost}
     * @param limit - max number of results
     * @param location - current location of the user
     * @param offset - post offset
     * @return {@link List} of the existing posts
     */
    public List<PlainPost> getPlainPostsRange(final Location location, final int limit, final long offset);

    /**
     * Lists every existing {@link PlainPost} for a given {@link Category}
     * @param limit - max number of results
     * @param offset - post offset
     * @param category - Category the posts belongs to
     * @return {@link List} of the existing posts
     */
    public List<PlainPost> getPlainPostsByCategoryRange(final Category category,final int limit, final long offset);

    /**
     * Lists every existing {@link PlainPost} for a given {@link Category}
     * @param limit - max number of results
     * @param offset - post offset
     * @param location - current location of the user
     * @param category - Category the posts belongs to
     * @return {@link List} of the existing posts
     */
    public List<PlainPost> getPlainPostsByCategoryRange(final Location location, final Category category,final int limit, final long offset);

    /**
     * Retrieves a {@link Post.PostBuilder} with every attribute set except for
     * the familyComments.
     * @param postId - ID of the post
     * @return Post with the associated ID of null if it doesn't exist
     */
    public Post.PostBuilder getFullPostById(final long postId);

    /**
     * Retrieves a {@link Post.PostBuilder} with every attribute set except for
     * the familyComments.
     * @param postId - ID of the post
     * @param location - current location of the user
     * @return Post with the associated ID of null if it doesn't exist
     */
    public Post.PostBuilder getFullPostById(final long postId, final Location location);

    /**
     * Retrieves a {@link Post} as a {@link PlainPost}.
     * @param postId - ID of the post
     * @return Plain Post with the associated ID or null if it doesn't exist
     */
    public PlainPost getPlainPostById(final long postId);

    /**
     * Deletes a {@link Post} from the database.
     * @param postId - ID of the post to delete
     * @return true if a product was deleted
     */
    public boolean deletePostById(final long postId);

    /**
     * Lists every nearby in range KM existing {@link Post} as a {@link PlainPost} .
     * @param location - current location of the user
     * @param range - max range of search in meters
     * @param limit - max number of results
     * @param offset - post offset
     * @return {@link List} of the existing posts
     */
    public List<PlainPost> getPlainPostsRange(final Location location, final int range,
                                              final int limit, final long offset);

    /**
     * Lists every nearby in range (Meters) existing {@link Post} as a {@link PlainPost} for a given {@link Category}
     * @param location - current location of the user
     * @param range - max range of search in meters
     * @param category - Category the posts belongs to
     * @param limit - max number of results
     * @param offset - post offset
     * @return {@link List} of the existing posts
     */
    public List<PlainPost> getPlainPostsByCategoryRange(final Location location, final int range, final Category category,
                                                        final int limit, final long offset);

    /**
     * Retrieves a {@link List} of {@link PlainPost} given a keyword ordered by id
     * The keyword should match the post title or description
     * @param keyword - The keyword which should be matched
     * @param limit - max number of results
     * @param offset - post offset
     * @return The list of plain post that match with the keyword (distance is set to 0)
     */
    public List<PlainPost> getPlainPostsByKeywordRange(final String keyword, final int limit, final long offset);

    /**
     * Retrieves a {@link List} of {@link PlainPost} given a keyword ordered by the distance descendent
     * The keyword should match the post title or description
     * @param keyword - The keyword which should be matched
     * @param location - Current user location
     * @param limit - max number of results
     * @param offset - post offset
     * @return The list of plain post that match with the keyword.
     */
    public List<PlainPost> getPlainPostsByKeywordRange(final String keyword, final Location location,
                                                       final int limit, final long offset);
    /**
     * Retrieves a {@link List} of {@link PlainPost} given a keyword ordered by id
     * The keyword should match the post title or description
     * @param keyword - The keyword which should be matched
     * @param category - The category we are searching in
     * @param limit - max number of results
     * @param offset - post offset
     * @return The list of plain post that match with the keyword (distance is set to 0)
     */
    public List<PlainPost> getPlainPostsByKeywordRange(final String keyword, final Category category,final int limit, final long offset);

    /**
     * Retrieves a {@link List} of {@link PlainPost} given a keyword ordered by the distance descendent
     * The keyword should match the post title or description
     * @param keyword - The keyword which should be matched
     * @param location - Current user location
     * @param category - The category we are searching in
     * @param limit - max number of results
     * @param offset - post offset
     * @return The list of plain post that match with the keyword.
     */
    public List<PlainPost> getPlainPostsByKeywordRange(final String keyword, final Location location, final Category category,final int limit, final long offset);

    /**
     * Lists post created by {@link User} as a {@link PlainPost} with the given userId.
     * @param userId - ID of the creator
     * @param limit - max number of results
     * @param offset - post offset
     * @return List of post. Empty in case the user did not create any post (distance is set to 0)
     */
    public List<PlainPost> getPlainPostsByUserIdRange(final long userId, final int limit, final long offset);

    /**
     * Lists post created by {@link User} as a {@link PlainPost} with the given userId.
     * @param userId - ID of the creator
     * @param location - current location of the user
     * @param limit - max number of results
     * @param offset - post offset
     * @return List of post. Empty in case the user did not create any post
     */
    public List<PlainPost> getPlainPostsByUserIdRange(final long userId, final Location location,
                                                      final int limit, final long offset);

    /**
     * Lists post created by {@link User} as a {@link PlainPost} with the given userId.
     * @param userId - ID of the creator
     * @param category - category we are searching
     * @param limit - max number of results
     * @param offset - post offset
     * @return List of post. Empty in case the user did not create any post (distance is set to 0)
     */
    public List<PlainPost> getPlainPostsByUserIdRange(final long userId,final Category category,
                                                      final int limit, final long offset);

    /**
     * Lists post created by {@link User} as a {@link PlainPost} with the given userId.
     * @param userId - ID of the creator
     * @param location - current location of the user
     * @param category - category we are searching
     * @param limit - max number of results
     * @param offset - post offset
     * @return List of post. Empty in case the user did not create any post
     */
    public List<PlainPost> getPlainPostsByUserIdRange(final long userId, final Location location,final Category category,
                                                      final int limit, final long offset);

    /**
     * Retrieves the total amount of post registered.
     * @return The number of posts.
     */
    public long getTotalPosts();

    /**
     * Retrieves the total amount of post nearby.
     * @param location - current location of the user
     * @param range - max range of search
     * @return The number of posts.
     */
    public long getTotalPosts(final Location location,final int range);

    /**
     * Retrieves the total amount of post registered for a given {@link Category}
     * @param category - Category the posts belongs to
     * @return The number of posts.
     */
    public long getTotalPostsByCategory(final Category category);

    /**
     * Retrieves the total amount of post nearby for a given {@link Category}
     * @param location - current location of the user
     * @param range - max range of search
     * @param category - Category the posts belongs to
     * @return The number of posts.
     */
    public long getTotalPostsByCategory(final Location location,final int range,final Category category);

    /**
     * Retrieves the total amount of post registered for a given keyword
     * @param keyword - keyword search
     * @return The number of posts.
     */
    public long getTotalPostsByKeyword(final String keyword);

    /**
     * Retrieves the total amount of post registered for a given {@link Category} and keyword
     * @param keyword - keyword search
     * @param category - the category we are searching
     * @return The number of posts.
     */
    public long getTotalPostsByKeyword(final String keyword,final Category category);

    /**
     * Retrieves the total amount of post registered for a given {@link User}
     * @param userId - the user id
     * @return The number of posts.
     */
    public long getTotalPostsByUserId(final long userId);

    /**
     * Retrieves the total amount of post registered for a given {@link User}
     * @param userId - the user id
     * @param category - the category we are searching
     * @return The number of posts.
     */
    public long getTotalPostsByUserId(final long userId,final Category category);


}
