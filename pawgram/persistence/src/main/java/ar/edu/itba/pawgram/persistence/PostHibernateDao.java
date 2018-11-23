package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.exception.InvalidPostException;
import ar.edu.itba.pawgram.interfaces.persistence.PostDao;
import ar.edu.itba.pawgram.model.*;
import ar.edu.itba.pawgram.model.structures.Location;
import ar.edu.itba.pawgram.persistence.querybuilder.PostKeywordQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import javax.persistence.*;
import java.util.*;

@Repository
public class PostHibernateDao implements PostDao {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PostKeywordQueryBuilder postKeywordQueryBuilder;

    @Override
    public Post createPost(String title, String description, String contact_phone, Date event_date,
                           Category category, Pet pet, boolean is_male, Location location, User owner){
        final Post.PostBuilder postBuilder = Post.getBuilder(title, Collections.emptyList());

        final Post post = postBuilder.description(description).contact_phone(contact_phone).
                event_date(event_date).category(category).pet(pet).is_male(is_male).location(location).user(owner).build();
        em.persist(post);

        return post;
    }

    @Override
    public Post modifyPost(long postId, String title, String description, String contact_phone, Date event_date, Category category, Pet pet,
                           Boolean is_male, Location location) throws InvalidPostException {
        final Post p = getPlainPostById(postId);
        if(p == null)
            throw new InvalidPostException();

        Post.PostBuilder postBuilder = Post.getBuilderFromPost(p);
        postBuilder = postBuilder.title(title).description(description).contact_phone(contact_phone).category(category).pet(pet).is_male(is_male).location(location);
        if(category != Category.ADOPT){
           postBuilder = postBuilder.event_date(event_date);
        }

        Post modifiedPost = postBuilder.build();
        em.merge(modifiedPost);
        return modifiedPost;
    }

    @Override
    public List<Post> getPlainPostsRange(int limit, int offset) {
        final TypedQuery<Post> query = em.createQuery("select p from Post as p ORDER BY p.id ASC", Post.class);
        return pagedResult(query, offset, limit);
    }

    @Override
    public List<Post> getPlainPostsRange(Location location, int limit, int offset) {
        final TypedQuery<Post> query = em.createQuery("select p from Post as p " +
                " ORDER BY haversine_distance(:lat1,:lon1,p.location.latitude,p.location.longitude) ASC", Post.class);
        query.setParameter("lat1",location.getLatitude());
        query.setParameter("lon1",location.getLongitude());

        return pagedResult(query, offset, limit);
    }

    @Override
    public List<Post> getPlainPostsByCategoryRange(Category category, int limit, int offset) {
        final TypedQuery<Post> query = em.createQuery("select p from Post as p WHERE p.category = :category ORDER BY p.id ASC", Post.class);
        query.setParameter("category", category);
        return pagedResult(query, offset, limit);
    }

    @Override
    public List<Post> getPlainPostsByCategoryRange(Location location, Category category, int limit, int offset) {
        final TypedQuery<Post> query = em.createQuery("select p from Post as p WHERE p.category = :category " +
                " ORDER BY haversine_distance(:lat1,:lon1,p.location.latitude,p.location.longitude) ASC", Post.class);
        query.setParameter("category", category);
        query.setParameter("lat1",location.getLatitude());
        query.setParameter("lon1",location.getLongitude());

        return pagedResult(query, offset, limit);
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
        post.getUserSubscriptions().size();//check if this works

        return post;
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
        final TypedQuery<Post> query = em.createQuery("select p from Post as p WHERE haversine_distance(:lat1,:lon1,p.location.latitude,p.location.longitude) < :range" +
                " ORDER BY haversine_distance(:lat1,:lon1,p.location.latitude,p.location.longitude) ASC", Post.class);
        query.setParameter("lat1",location.getLatitude());
        query.setParameter("lon1",location.getLongitude());
        query.setParameter("range",range);

        return pagedResult(query, offset, limit);
    }

    @Override
    public List<Post> getPlainPostsByCategoryRange(Location location, int range, Category category, int limit, int offset) {
        final TypedQuery<Post> query = em.createQuery("select p from Post as p WHERE p.category = :category AND " +
                " haversine_distance(:lat1,:lon1,p.location.latitude,p.location.longitude) < :range " +
                " ORDER BY haversine_distance(:lat1,:lon1,p.location.latitude,p.location.longitude) ASC", Post.class);
        query.setParameter("category",category);
        query.setParameter("lat1",location.getLatitude());
        query.setParameter("lon1",location.getLongitude());
        query.setParameter("range",range);

        return pagedResult(query, offset, limit);
    }

    @Override
    public List<Post> getPlainPostsByKeywordRange(Set<String> keywords, int limit, int offset) {
        final Map<String, String> keyWordsRegExp = new HashMap<>();
        final String whereQuery = postKeywordQueryBuilder.buildQuery(keywords, keyWordsRegExp);

        final TypedQuery<Post> query = em.createQuery("from Post as p where (" + whereQuery + ") ORDER BY p.id ASC", Post.class);

        for (final Map.Entry<String, String> e : keyWordsRegExp.entrySet())
            query.setParameter(e.getKey(), e.getValue());

        return pagedResult(query, offset, limit);
    }

    @Override
    public List<Post> getPlainPostsByKeywordRange(Set<String> keywords, Location location, int limit, int offset) {
        final Map<String, String> keyWordsRegExp = new HashMap<>();
        final String whereQuery = postKeywordQueryBuilder.buildQuery(keywords, keyWordsRegExp);

        final TypedQuery<Post> query = em.createQuery("from Post as p where (" + whereQuery +
                ") ORDER BY haversine_distance(:lat1,:lon1,p.location.latitude,p.location.longitude) ASC", Post.class);
        query.setParameter("lat1",location.getLatitude());
        query.setParameter("lon1",location.getLongitude());

        for (final Map.Entry<String, String> e : keyWordsRegExp.entrySet())
            query.setParameter(e.getKey(), e.getValue());

        return pagedResult(query, offset, limit);
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
        final Map<String, String> keyWordsRegExp = new HashMap<>();
        final String whereQuery = postKeywordQueryBuilder.buildQuery(keywords, keyWordsRegExp);

        final TypedQuery<Post> query = em.createQuery("from Post as p where p.category = :category AND (" + whereQuery +
                ") ORDER BY haversine_distance(:lat1,:lon1,p.location.latitude,p.location.longitude) ASC", Post.class);
        query.setParameter("lat1",location.getLatitude());
        query.setParameter("lon1",location.getLongitude());

        for (final Map.Entry<String, String> e : keyWordsRegExp.entrySet())
            query.setParameter(e.getKey(), e.getValue());
        return pagedResult(query, offset, limit);
    }

    @Override
    public long getTotalPosts() {
        final TypedQuery<Long> query = em.createQuery("select count(*) from Post as p", Long.class);

        final Long total = query.getSingleResult();
        return total != null ? total : 0;
    }

    @Override
    public long getTotalPosts(Location location, int range) {
        final TypedQuery<Long> query = em.createQuery("select count(*) from Post as p WHERE " +
                " haversine_distance(:lat1,:lon1,p.location.latitude,p.location.longitude) < :range", Long.class);
        query.setParameter("lat1",location.getLatitude());
        query.setParameter("lon1",location.getLongitude());
        query.setParameter("range",range);

        final Long total = query.getSingleResult();
        return total != null ? total : 0;
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
        final TypedQuery<Long> query = em.createQuery("select count(*) from Post as p WHERE p.category = :category AND " +
                " haversine_distance(:lat1,:lon1,p.location.latitude,p.location.longitude) < :range", Long.class);
        query.setParameter("category", category);
        query.setParameter("lat1",location.getLatitude());
        query.setParameter("lon1",location.getLongitude());
        query.setParameter("range",range);

        final Long total = query.getSingleResult();
        return total != null ? total : 0;
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

    private List<Post> pagedResult(final TypedQuery<Post> query, final int offset, final int length) {
        query.setFirstResult(offset);
        query.setMaxResults(length);
        return query.getResultList();
    }
}
