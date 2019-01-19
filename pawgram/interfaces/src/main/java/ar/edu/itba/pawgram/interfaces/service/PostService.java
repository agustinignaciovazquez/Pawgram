package ar.edu.itba.pawgram.interfaces.service;

import ar.edu.itba.pawgram.interfaces.exception.InvalidPostException;
import ar.edu.itba.pawgram.interfaces.exception.InvalidQueryException;
import ar.edu.itba.pawgram.interfaces.exception.PostCreateException;
import ar.edu.itba.pawgram.model.*;
import ar.edu.itba.pawgram.model.structures.Location;
import ar.edu.itba.pawgram.model.Pet;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PostService {
    public final static int MIN_WORD_SIZE = 3;
    public final static int MAX_WORD_SIZE = 64;
    public final static  int MAX_IMAGES = 4;

    /**
     * Creates a {@link Post} inserting the {@link Post} data into the database.
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
    public Post createPost(final String title, final String description, final List<byte[]> raw_images, String contact_phone, Date event_date,
                                                     Category category, Pet pet, boolean is_male, Location location, User owner) throws PostCreateException, InvalidPostException;
    /**
     * Modifies a {@link Post} updateing the {@link Post} data into the database.
     * @param postId - Post id to modify
     * @param title - New title of the post
     * @param raw_images - New raw img's
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
    public Post modifyPost(final long postId, final List<byte[]> raw_images, final String title, final String description, final String contact_phone, final Date event_date,
                           final Category category, final Pet pet, final Boolean is_male, final Location location) throws InvalidPostException, PostCreateException;
    /**
     * Lists every existing {@link Post} for a given {@link Category}
     * @param page - number of page
     * @param pageSize - max number of results per page
     * @param location - current location of the user
     * @param category - Category the posts belongs to
     * @return {@link List} of the existing posts
     */
    public List<Post> getPlainPostsPaged(final Optional<Location> location, final Optional<Category> category, final int page, final int pageSize);

    /**
     * Retrieves a {@link ar.edu.itba.pawgram.model.Post.PostBuilder} with every attribute set
     * @param postId - ID of the post
     * @return Post with the associated ID of null if it doesn't exist
     */
    public Post getFullPostById(final long postId);

    /**
     * Retrieves a {@link ar.edu.itba.pawgram.model.Post.PostBuilder} with every attribute set
     * @param postId - ID of the post
     * @param location - current location of the user
     * @return Post with the associated ID of null if it doesn't exist
     */
    public Post getFullPostById(final long postId, final Location location);

    /**
     * Retrieves a {@link ar.edu.itba.pawgram.model.Post} as a {@link Post}.
     * @param postId - ID of the post
     * @return Plain Post with the associated ID or null if it doesn't exist
     */
    public Post getPlainPostById(final long postId);

    /**
     * Lists every nearby in range (Meters) existing {@link ar.edu.itba.pawgram.model.Post} as a {@link Post} for a given {@link Category}
     * @param location - current location of the user
     * @param range - max range of search in meters
     * @param category - Category the posts belongs to
     * @param page - number of page
     * @param pageSize - max number of results per page
     * @return {@link List} of the existing posts
     */
    public List<Post> getPlainPostsPaged(final Location location, final int range, final Optional<Category> category,
                                                   final int page, final int pageSize);

    /**
     * Retrieves a {@link List} of {@link Post} given a keyword ordered by the distance descendent
     * The keyword should match the post title or description
     * @param keyword - The keyword which should be matched
     * @param category - The category we are searching in
     * @param location - Current user location
     * @param page - number of page
     * @param pageSize - max number of results per page
     * @return The list of plain post that match with the keyword.
     */
    public List<Post> getPlainPostsByKeywordPaged(final String keyword, final Optional<Location> location, final Optional<Category> category, final int page, final int pageSize) throws InvalidQueryException;

    /**
     * Deletes a {@link ar.edu.itba.pawgram.model.Post} from the database.
     * @param postId - ID of the post to delete
     * @return true if a post was deleted
     */
    public boolean deletePostById(final long postId);

    /**
     * Retrieves the total amount of post registered for a given {@link Category}
     * @param category - Category the posts belongs to
     * @return The number of posts.
     */
    public long getTotalPosts(final Optional<Category> category);

    /**
     * Retrieves the total amount of post nearby for a given {@link Category}
     * @param location - current location of the user
     * @param range - max range of search
     * @param category - Category the posts belongs to
     * @return The number of posts.
     */
    public long getTotalPosts(final Location location,final int range,final Optional<Category> category);

    /**
     * Retrieves the total amount of post registered for a given {@link Category} and keyword
     * @param keyword - keyword search
     * @param category - the category we are searching
     * @return The number of posts.
     */
    public long getTotalPostsByKeyword(final String keyword,final Optional<Category> category) throws InvalidQueryException;

    /**
     * Retrieves the max page of post registered for a given {@link Category}
     * @param category - Category the posts belongs to
     * @param pageSize - the quantity of post per page
     * @return The number of posts.
     */
    public long getMaxPage(final int pageSize, final Optional<Category> category);

    /**
     * Retrieves the max page of post nearby for a given {@link Category}
     * @param location - current location of the user
     * @param range - max range of search
     * @param pageSize - the quantity of post per page
     * @param category - Category the posts belongs to
     * @return The number of posts.
     */
    public long getMaxPage(final int pageSize, final Location location,final int range,final Optional<Category> category);

    /**
     * Retrieves the max page of post registered for a given {@link Category} and keyword
     * @param keyword - keyword search
     * @param category - the category we are searching
     * @param pageSize - the quantity of post per page
     * @return The number of posts.
     */
    public long getMaxPageByKeyword(final int pageSize, final String keyword,final Optional<Category> category) throws InvalidQueryException;


}
