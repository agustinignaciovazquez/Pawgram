package ar.edu.itba.pawgram.interfaces.service;

import ar.edu.itba.pawgram.model.*;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;

import java.time.LocalDateTime;
import java.util.List;

public interface PostService {
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
     * @param owner - owner id of the post
     * @return Post - The newly created post
     */
    public Post createPost(final String title, final String description, final String img_url, final String contact_phone,
                                       final LocalDateTime event_date, final Category category, final Pet pet, final boolean is_male,
                                       final Location location, final User owner);

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
     * Retrieves a {@link List} of {@link PlainPost} given a keyword ordered by the distance descendent
     * The keyword should match the post title or description
     * @param keyword - The keyword which should be matched
     * @param location - Current user location
     * @param category - The category we are searching in
     * @return The list of plain post that match with the keyword.
     */
    public List<PlainPost> getPlainPostsByKeyword(final String keyword, final Location location, final Category category);

    /**
     * Lists post created by {@link User} as a {@link PlainPost} with the given userId.
     * @param userId - ID of the creator
     * @param location - current location of the user
     * @return List of post. Empty in case the user did not create any post
     */
    public List<PlainPost> getPlainPostsByUserId(final long userId, final Location location);

    /**
     * Lists post created by {@link User} as a {@link PlainPost} with the given userId.
     * @param userId - ID of the creator
     * @param category - The category we are searching in
     * @param location - current location of the user
     * @return List of post. Empty in case the user did not create any post
     */
    public List<PlainPost> getPlainPostsByUserId(final long userId, final Category category, final Location location);

    /**
     * Retrieves a {@link Post.PostBuilder} with every attribute set
     * @param postId - ID of the post
     * @return Post with the associated ID of null if it doesn't exist
     */
    public Post getFullPostById(final long postId);

    /**
     * Retrieves a {@link Post.PostBuilder} with every attribute set
     * @param postId - ID of the post
     * @param location - current location of the user
     * @return Post with the associated ID of null if it doesn't exist
     */
    public Post getFullPostById(final long postId, final Location location);

    /**
     * Retrieves a {@link Post} as a {@link PlainPost}.
     * @param postId - ID of the post
     * @return Plain Post with the associated ID or null if it doesn't exist
     */
    public PlainPost getPlainPostById(final long postId);

    /**
     * Lists every nearby in range KM existing {@link Post} as a {@link PlainPost} .
     * @param location - current location of the user
     * @param range - max range of search in meters
     * @param page - number of page
     * @param pageSize - max number of results per page
     * @return {@link List} of the existing posts
     */
    public List<PlainPost> getPlainPostsPaged(final Location location, final int range,
                                              final long page, final int pageSize);

    /**
     * Lists every nearby in range (Meters) existing {@link Post} as a {@link PlainPost} for a given {@link Category}
     * @param location - current location of the user
     * @param range - max range of search in meters
     * @param category - Category the posts belongs to
     * @param page - number of page
     * @param pageSize - max number of results per page
     * @return {@link List} of the existing posts
     */
    public List<PlainPost> getPlainPostsByCategoryPaged(final Location location, final int range, final Category category,
                                                        final long page, final int pageSize);

    /**
     * Retrieves a {@link List} of {@link PlainPost} given a keyword ordered by the id asc
     * The keyword should match the post title or description
     * @param keyword - The keyword which should be matched
     * @param page - number of page
     * @param pageSize - max number of results per page
     * @return The list of plain post that match with the keyword.
     */
    public List<PlainPost> getPlainPostsByKeywordPaged(final String keyword, final long page, final int pageSize);

    /**
     * Retrieves a {@link List} of {@link PlainPost} given a keyword ordered by the distance descendent
     * The keyword should match the post title or description
     * @param keyword - The keyword which should be matched
     * @param location - Current user location
     * @param page - number of page
     * @param pageSize - max number of results per page
     * @return The list of plain post that match with the keyword.
     */
    public List<PlainPost> getPlainPostsByKeywordPaged(final String keyword, final Location location,
                                                       final long page, final int pageSize);

    /**
     * Retrieves a {@link List} of {@link PlainPost} given a keyword ordered by the id asc
     * The keyword should match the post title or description
     * @param keyword - The keyword which should be matched
     * @param category - The category we are searching in
     * @param page - number of page
     * @param pageSize - max number of results per page
     * @return The list of plain post that match with the keyword.
     */
    public List<PlainPost> getPlainPostsByKeywordPaged(final String keyword, final Category category, final long page, final int pageSize);

    /**
     * Retrieves a {@link List} of {@link PlainPost} given a keyword ordered by the distance descendent
     * The keyword should match the post title or description
     * @param keyword - The keyword which should be matched
     * @param category - The category we are searching in
     * @param location - Current user location
     * @param page - number of page
     * @param pageSize - max number of results per page
     * @return The list of plain post that match with the keyword.
     */
    public List<PlainPost> getPlainPostsByKeywordPaged(final String keyword, final Location location, final Category category, final long page, final int pageSize);

    /**
     * Lists post created by {@link User} as a {@link PlainPost} with the given userId.
     * @param userId - ID of the creator
     * @param page - number of page
     * @param pageSize - max number of results per page
     * @return List of post. Empty in case the user did not create any post
     */
    public List<PlainPost> getPlainPostsByUserIdPaged(final long userId, final long page, final int pageSize);

    /**
     * Lists post created by {@link User} as a {@link PlainPost} with the given userId.
     * @param userId - ID of the creator
     * @param location - current location of the user
     * @param page - number of page
     * @param pageSize - max number of results per page
     * @return List of post. Empty in case the user did not create any post
     */
    public List<PlainPost> getPlainPostsByUserIdPaged(final long userId, final Location location,
                                                      final long page, final int pageSize);

    /**
     * Lists post created by {@link User} as a {@link PlainPost} with the given userId.
     * @param userId - ID of the creator
     * @param category - category we are searching
     * @param page - number of page
     * @param pageSize - max number of results per page
     * @return List of post. Empty in case the user did not create any post
     */
    public List<PlainPost> getPlainPostsByUserIdPaged(final long userId, final Category category,
                                                      final long page, final int pageSize);

    /**
     * Lists post created by {@link User} as a {@link PlainPost} with the given userId.
     * @param userId - ID of the creator
     * @param location - current location of the user
     * @param category - category we are searching
     * @param page - number of page
     * @param pageSize - max number of results per page
     * @return List of post. Empty in case the user did not create any post
     */
    public List<PlainPost> getPlainPostsByUserIdPaged(final long userId, final Location location,final Category category,
                                                      final long page, final int pageSize);

    /**
     * Deletes a {@link Post} from the database.
     * @param postId - ID of the post to delete
     * @return true if a product was deleted
     */
    public boolean deletePostById(final long postId);

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

    /**
     * Retrieves the max page of post nearby.
     * @param pageSize - the quantity of post per page
     * @return The number of posts.
     */
    public long getMaxPage(final int pageSize);

    /**
     * Retrieves the max page of post nearby.
     * @param location - current location of the user
     * @param pageSize - the quantity of post per page
     * @param range - max range of search
     * @return The number of posts.
     */
    public long getMaxPage(final int pageSize, final Location location,final int range);

    /**
     * Retrieves the max page of post registered for a given {@link Category}
     * @param category - Category the posts belongs to
     * @param pageSize - the quantity of post per page
     * @return The number of posts.
     */
    public long getMaxPageByCategory(final int pageSize, final Category category);

    /**
     * Retrieves the max page of post nearby for a given {@link Category}
     * @param location - current location of the user
     * @param range - max range of search
     * @param pageSize - the quantity of post per page
     * @param category - Category the posts belongs to
     * @return The number of posts.
     */
    public long getMaxPageByCategory(final int pageSize, final Location location,final int range,final Category category);

    /**
     * Retrieves the max page of post registered for a given keyword
     * @param keyword - keyword search
     * @param pageSize - the quantity of post per page
     * @return The number of posts.
     */
    public long getMaxPageByKeyword(final int pageSize, final String keyword);

    /**
     * Retrieves the max page of post registered for a given {@link Category} and keyword
     * @param keyword - keyword search
     * @param category - the category we are searching
     * @param pageSize - the quantity of post per page
     * @return The number of posts.
     */
    public long getMaxPageByKeyword(final int pageSize, final String keyword,final Category category);

    /**
     * Retrieves the max page of post registered for a given {@link User}
     * @param userId - the user id
     * @param pageSize - the quantity of post per page
     * @return The number of posts.
     */
    public long getMaxPageByUserId(final int pageSize, final long userId);

    /**
     * Retrieves the max page of post registered for a given {@link User}
     * @param userId - the user id
     * @param pageSize - the quantity of post per page
     * @param category - the category we are searching
     * @return The number of posts.
     */
    public long getMaxPageByUserId(final int pageSize, final long userId,final Category category);
}
