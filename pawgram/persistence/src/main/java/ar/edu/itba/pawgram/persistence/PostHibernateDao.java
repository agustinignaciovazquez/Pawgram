package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.exception.PostCreateException;
import ar.edu.itba.pawgram.interfaces.persistence.PostDao;
import ar.edu.itba.pawgram.model.Category;
import ar.edu.itba.pawgram.model.Pet;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;
import ar.edu.itba.pawgram.model.structures.Location;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

public class PostHibernateDao implements PostDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Post.PostBuilder createPost(String title, String description,
                                       List<byte[]> raw_images, String contact_phone, LocalDateTime event_date,
                                       Category category, Pet pet, boolean is_male, Location location, User owner) throws PostCreateException {
        return null;
    }

    @Override
    public List<PlainPost> getPlainPostsRange(int limit, long offset) {
        return null;
    }

    @Override
    public List<PlainPost> getPlainPostsRange(Location location, int limit, long offset) {
        return null;
    }

    @Override
    public List<PlainPost> getPlainPostsByCategoryRange(Category category, int limit, long offset) {
        return null;
    }

    @Override
    public List<PlainPost> getPlainPostsByCategoryRange(Location location, Category category, int limit, long offset) {
        return null;
    }

    @Override
    public Post.PostBuilder getFullPostById(long postId) {
        return null;
    }

    @Override
    public Post.PostBuilder getFullPostById(long postId, Location location) {
        return null;
    }

    @Override
    public PlainPost getPlainPostById(long postId) {
        return null;
    }

    @Override
    public boolean deletePostById(long postId) {
        return false;
    }

    @Override
    public List<PlainPost> getPlainPostsRange(Location location, int range, int limit, long offset) {
        return null;
    }

    @Override
    public List<PlainPost> getPlainPostsByCategoryRange(Location location, int range, Category category, int limit, long offset) {
        return null;
    }

    @Override
    public List<PlainPost> getPlainPostsByKeywordRange(String keyword, int limit, long offset) {
        return null;
    }

    @Override
    public List<PlainPost> getPlainPostsByKeywordRange(String keyword, Location location, int limit, long offset) {
        return null;
    }

    @Override
    public List<PlainPost> getPlainPostsByKeywordRange(String keyword, Category category, int limit, long offset) {
        return null;
    }

    @Override
    public List<PlainPost> getPlainPostsByKeywordRange(String keyword, Location location, Category category, int limit, long offset) {
        return null;
    }

    @Override
    public List<PlainPost> getPlainPostsByUserIdRange(long userId, int limit, long offset) {
        return null;
    }

    @Override
    public List<PlainPost> getPlainPostsByUserIdRange(long userId, Location location, int limit, long offset) {
        return null;
    }

    @Override
    public List<PlainPost> getPlainPostsByUserIdRange(long userId, Category category, int limit, long offset) {
        return null;
    }

    @Override
    public List<PlainPost> getPlainPostsByUserIdRange(long userId, Location location, Category category, int limit, long offset) {
        return null;
    }

    @Override
    public long getTotalPosts() {
        return 0;
    }

    @Override
    public long getTotalPosts(Location location, int range) {
        return 0;
    }

    @Override
    public long getTotalPostsByCategory(Category category) {
        return 0;
    }

    @Override
    public long getTotalPostsByCategory(Location location, int range, Category category) {
        return 0;
    }

    @Override
    public long getTotalPostsByKeyword(String keyword) {
        return 0;
    }

    @Override
    public long getTotalPostsByKeyword(String keyword, Category category) {
        return 0;
    }

    @Override
    public long getTotalPostsByUserId(long userId) {
        return 0;
    }

    @Override
    public long getTotalPostsByUserId(long userId, Category category) {
        return 0;
    }
}
