package ar.edu.itba.pawgram.interfaces.persistence;

import ar.edu.itba.pawgram.interfaces.exception.InvalidPostException;
import ar.edu.itba.pawgram.interfaces.exception.PostCreateException;
import ar.edu.itba.pawgram.model.*;
import ar.edu.itba.pawgram.model.structures.Location;
import ar.edu.itba.pawgram.model.Pet;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface PostDao {
    /**
     * Creates a {@link Post} inserting the {@link Post} data into the database.
     * @param title - Title of the post
     * @param description - Description of the post
     * @param contact_phone - contact phone of the owner
     * @param category - Category the post belongs to
     * @param event_date - Date of the event
     * @param pet - type of pet the post is for
     * @param is_male - true if the pet is male, false otherwise
     * @param location - location of the event
     * @param owner - owner id of the post
     * @return Post - The newly created post
     */
    public Post createPost(String title, String description,  String contact_phone, Date event_date,
                           Category category, Pet pet, boolean is_male, Location location, User owner);

    /**
     * Modifies a {@link Post} updateing the {@link Post} data into the database.
     * @param postId - Post id to modify
     * @param title - New title of the post
     * @param description - New description of the post
     * @param contact_phone - New contact phone of the owner
     * @param category - New Category the post belongs to
     * @param event_date - New Date of the event
     * @param pet - New type of pet the post is for
     * @param is_male - New true if the pet is male, false otherwise
     * @param location - New location of the event
     * @return Post - The modified post
     * (note that if any parameter is null, no change would take effect in such field due to builder implementation)
     */
    public Post modifyPost(final long postId, final String title, final String description,  final String contact_phone, final Date event_date,
                           final Category category, final Pet pet, final Boolean is_male, final Location location) throws InvalidPostException;

    /**
     * Lists every post {@link Post}
     * @param limit - max number of results
     * @param offset - post offset
     * @return {@link List} of the existing posts (distance as 0)
     */
    public List<Post> getPlainPostsRange(final int limit, final int offset);

    /**
     * Lists every post {@link Post}
     * @param limit - max number of results
     * @param location - current location of the user
     * @param offset - post offset
     * @return {@link List} of the existing posts
     */
    public List<Post> getPlainPostsRange(final Location location, final int limit, final int offset);

    /**
     * Lists every existing {@link Post} for a given {@link Category}
     * @param limit - max number of results
     * @param offset - post offset
     * @param category - Category the posts belongs to
     * @return {@link List} of the existing posts
     */
    public List<Post> getPlainPostsByCategoryRange(final Category category,final int limit, final int offset);

    /**
     * Lists every existing {@link Post} for a given {@link Category}
     * @param limit - max number of results
     * @param offset - post offset
     * @param location - current location of the user
     * @param category - Category the posts belongs to
     * @return {@link List} of the existing posts
     */
    public List<Post> getPlainPostsByCategoryRange(final Location location, final Category category,final int limit, final int offset);

    /**
     * Retrieves a {@link Post.PostBuilder} with every attribute set except for
     * the familyComments.
     * @param postId - ID of the post
     * @return Post with the associated ID of null if it doesn't exist
     */
    public Post getFullPostById(final long postId);

    /** DEPRECATED
     * Retrieves a {@link Post.PostBuilder} with every attribute set except for
     * the familyComments.
     * @param postId - ID of the post
     * @param location - current location of the user
     * @return Post with the associated ID of null if it doesn't exist

    public Post getFullPostById(final long postId, final Location location);*/

    /**
     * Retrieves a {@link Post} as a {@link Post}.
     * @param postId - ID of the post
     * @return Plain Post with the associated ID or null if it doesn't exist
     */
    public Post getPlainPostById(final long postId);

    /**
     * Deletes a {@link Post} from the database.
     * @param postId - ID of the post to delete
     * @return true if a post was deleted
     */
    public boolean deletePostById(final long postId);


    /**
     * Lists every nearby in range KM existing {@link Post} as a {@link Post} .
     * @param location - current location of the user
     * @param range - max range of search in meters
     * @param limit - max number of results
     * @param offset - post offset
     * @return {@link List} of the existing posts
     */
    public List<Post> getPlainPostsRange(final Location location, final int range,
                                         final int limit, final int offset);

    /**
     * Lists every nearby in range (Meters) existing {@link Post} as a {@link Post} for a given {@link Category}
     * @param location - current location of the user
     * @param range - max range of search in meters
     * @param category - Category the posts belongs to
     * @param limit - max number of results
     * @param offset - post offset
     * @return {@link List} of the existing posts
     */
    public List<Post> getPlainPostsByCategoryRange(final Location location, final int range, final Category category,
                                                   final int limit, final int offset);

    /**
     * Retrieves a {@link List} of {@link Post} given a keyword ordered by id
     * The keyword should match the post title or description
     * @param keywords - The keyword which should be matched
     * @param limit - max number of results
     * @param offset - post offset
     * @return The list of plain post that match with the keyword (distance is set to 0)
     */
    public List<Post> getPlainPostsByKeywordRange(final Set<String> keywords, final int limit, final int offset);

    /**
     * Retrieves a {@link List} of {@link Post} given a keyword ordered by the distance descendent
     * The keyword should match the post title or description
     * @param keywords - The keyword which should be matched
     * @param location - Current user location
     * @param limit - max number of results
     * @param offset - post offset
     * @return The list of plain post that match with the keyword.
     */
    public List<Post> getPlainPostsByKeywordRange(final Set<String> keywords, final Location location,
                                                  final int limit, final int offset);
    /**
     * Retrieves a {@link List} of {@link Post} given a keyword ordered by id
     * The keyword should match the post title or description
     * @param keywords - The keyword which should be matched
     * @param category - The category we are searching in
     * @param limit - max number of results
     * @param offset - post offset
     * @return The list of plain post that match with the keyword (distance is set to 0)
     */
    public List<Post> getPlainPostsByKeywordRange(final Set<String> keywords, final Category category,final int limit, final int offset);

    /**
     * Retrieves a {@link List} of {@link Post} given a keyword ordered by the distance descendent
     * The keyword should match the post title or description
     * @param keywords - The keyword which should be matched
     * @param location - Current user location
     * @param category - The category we are searching in
     * @param limit - max number of results
     * @param offset - post offset
     * @return The list of plain post that match with the keyword.
     */
    public List<Post> getPlainPostsByKeywordRange(final Set<String> keywords, final Location location, final Category category,final int limit, final int offset);

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
     * @param keywords - keyword search
     * @return The number of posts.
     */
    public long getTotalPostsByKeyword(final Set<String> keywords);

    /**
     * Retrieves the total amount of post registered for a given {@link Category} and keyword
     * @param keywords - keyword search
     * @param category - the category we are searching
     * @return The number of posts.
     */
    public long getTotalPostsByKeyword(final Set<String> keywords,final Category category);




}
