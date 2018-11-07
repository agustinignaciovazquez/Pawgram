package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.exception.PostCreateException;
import ar.edu.itba.pawgram.interfaces.persistence.PostDao;
import ar.edu.itba.pawgram.model.Category;
import ar.edu.itba.pawgram.model.Pet;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.model.structures.Location;
import ar.edu.itba.pawgram.persistence.querybuilder.PostKeywordQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class PostHibernateDao implements PostDao {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PostKeywordQueryBuilder postKeywordQueryBuilder;

    @Override
    public Post createPost(String title, String description,
                           List<byte[]> raw_images, String contact_phone, LocalDateTime event_date,
                           Category category, Pet pet, boolean is_male, Location location, User owner) throws PostCreateException {
        final Post.PostBuilder postBuilder = Post.getBuilder(title, Collections.emptyList());

        final Post post = postBuilder.description(description).contact_phone(contact_phone).
                event_date(event_date).category(category).pet(pet).is_male(is_male).location(location).user(owner).build();
        em.persist(post);

        return post;
    }

    @Override
    public List<Post> getPlainPostsRange(int limit, int offset) {
        final TypedQuery<Post> query = em.createQuery("select p from Post as p ORDER BY p.id ASC", Post.class);
        return pagedResult(query, offset, limit);
    }

    @Override
    public List<Post> getPlainPostsRange(Location location, int limit, int offset) {
        return null;
    }

    @Override
    public List<Post> getPlainPostsByCategoryRange(Category category, int limit, int offset) {
        final TypedQuery<Post> query = em.createQuery("select p from Post as p WHERE p.category = :category ORDER BY p.id ASC", Post.class);
        query.setParameter("category", category);
        return pagedResult(query, offset, limit);
    }

    @Override
    public List<Post> getPlainPostsByCategoryRange(Location location, Category category, int limit, int offset) {
        return null;
    }

    @Override
    public Post getFullPostById(long postId) {
        final TypedQuery<Post> query = em.createQuery("from Post as p join fetch p.owner as pc where p.id = :postId", Post.class);
        query.setParameter("postId", postId);

        final List<Post> result = query.getResultList();

        if (result.isEmpty())
            return null;

        final Post post = result.get(0);

        // Hibernate lazy initialization
        post.getPostImages().size();

        return post;
    }

    @Override
    public Post getFullPostById(long postId, Location location) {
        return null;
    }

    @Override
    public Post getPlainPostById(long postId) {
        return em.find(Post.class,postId);
    }

    @Override
    public boolean deletePostById(long postId) {
        final Post post = getPlainPostById(postId);

        if (post != null)
            em.remove(post);

        return (post != null);
    }

    @Override
    public List<Post> getPlainPostsRange(Location location, int range, int limit, int offset) {
        return null;
    }

    @Override
    public List<Post> getPlainPostsByCategoryRange(Location location, int range, Category category, int limit, int offset) {
        return null;
    }

    @Override
    public List<Post> getPlainPostsByKeywordRange(Set<String> keywords, int limit, int offset) {
        final Map<String, String> keyWordsRegExp = new HashMap<>();
        final String whereQuery = postKeywordQueryBuilder.buildQuery(keywords, keyWordsRegExp);

        final TypedQuery<Post> query = em.createQuery("from Post as p where " + whereQuery + " ORDER BY p.id ASC", Post.class);

        for (final Map.Entry<String, String> e : keyWordsRegExp.entrySet())
            query.setParameter(e.getKey(), e.getValue());

        return pagedResult(query, offset, limit);
    }

    @Override
    public List<Post> getPlainPostsByKeywordRange(Set<String> keywords, Location location, int limit, int offset) {
        return null;
    }

    @Override
    public List<Post> getPlainPostsByKeywordRange(Set<String> keywords, Category category, int limit, int offset) {
        final Map<String, String> keyWordsRegExp = new HashMap<>();
        final String whereQuery = postKeywordQueryBuilder.buildQuery(keywords, keyWordsRegExp);

        final TypedQuery<Post> query = em.createQuery("from Post as p where p.category = :category AND (" + whereQuery + ") ORDER BY p.id ASC", Post.class);

        for (final Map.Entry<String, String> e : keyWordsRegExp.entrySet())
            query.setParameter(e.getKey(), e.getValue());

        return pagedResult(query, offset, limit);
    }

    @Override
    public List<Post> getPlainPostsByKeywordRange(Set<String> keywords, Location location, Category category, int limit, int offset) {
        return null;
    }

    @Override
    public List<Post> getPlainPostsByUserIdRange(long userId, int limit, int offset) {
        final TypedQuery<Post> query = em.createQuery("from Post as p where p.owner.id = :userId ORDER BY p.id ASC", Post.class);
        query.setParameter("userId", userId);

        return pagedResult(query, offset, limit);
    }

    @Override
    public List<Post> getPlainPostsByUserIdRange(long userId, Location location, int limit, int offset) {
        return null;
    }

    @Override
    public List<Post> getPlainPostsByUserIdRange(long userId, Category category, int limit, int offset) {
        final TypedQuery<Post> query = em.createQuery("from Post as p where p.category = :category AND p.owner.id = :userId ORDER BY p.id ASC", Post.class);
        query.setParameter("category", category);
        query.setParameter("userId", userId);

        return pagedResult(query, offset, limit);
    }

    @Override
    public List<Post> getPlainPostsByUserIdRange(long userId, Location location, Category category, int limit, int offset) {
        return null;
    }

    @Override
    public long getTotalPosts() {
        final TypedQuery<Long> query = em.createQuery("select count(*) from Post as p", Long.class);
        final Long total = query.getSingleResult();

        return total != null ? total : 0;
    }

    @Override
    public long getTotalPosts(Location location, int range) {
        return 0;
    }

    @Override
    public long getTotalPostsByCategory(Category category) {
        final TypedQuery<Long> query = em.createQuery("select count(*) from Post as p where p.category = :category", Long.class);
        query.setParameter("category", category);
        final Long total = query.getSingleResult();

        return total != null ? total : 0;
    }

    @Override
    public long getTotalPostsByCategory(Location location, int range, Category category) {
        return 0;
    }

    @Override
    public long getTotalPostsByKeyword(Set<String> keywords) {
        final Map<String, String> keyWordsRegExp = new HashMap<>();
        final String whereQuery = postKeywordQueryBuilder.buildQuery(keywords, keyWordsRegExp);

        final TypedQuery<Long> query = em.createQuery("select count(*) from Post as p where (" + whereQuery + ")", Long.class);

        for (final Map.Entry<String, String> e : keyWordsRegExp.entrySet())
            query.setParameter(e.getKey(), e.getValue());

        final Long total = query.getSingleResult();

        return total != null ? total : 0;
    }

    @Override
    public long getTotalPostsByKeyword(Set<String> keywords, Category category) {
        final Map<String, String> keyWordsRegExp = new HashMap<>();
        final String whereQuery = postKeywordQueryBuilder.buildQuery(keywords, keyWordsRegExp);

        final TypedQuery<Long> query = em.createQuery("select count(*) from Post as p where p.category = :category AND (" + whereQuery + ")", Long.class);

        query.setParameter("category", category);
        for (final Map.Entry<String, String> e : keyWordsRegExp.entrySet())
            query.setParameter(e.getKey(), e.getValue());

        final Long total = query.getSingleResult();

        return total != null ? total : 0;
    }

    @Override
    public long getTotalPostsByUserId(long userId) {
        final TypedQuery<Long> query = em.createQuery("select count(*) from Post as p where p.owner.id = :userId", Long.class);
        query.setParameter("userId",userId);
        final Long total = query.getSingleResult();

        return total != null ? total : 0;
    }

    @Override
    public long getTotalPostsByUserId(long userId, Category category) {
        final TypedQuery<Long> query = em.createQuery("select count(*) from Post as p where p.category = :category AND p.owner.id = :userId", Long.class);
        query.setParameter("category", category);
        query.setParameter("userId",userId);
        final Long total = query.getSingleResult();

        return total != null ? total : 0;
    }

    private List<Post> pagedResult(final TypedQuery<Post> query, final int offset, final int length) {
        query.setFirstResult(offset);
        query.setMaxResults(length);
        return query.getResultList();
    }
}
