package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.exception.InvalidPostException;
import ar.edu.itba.pawgram.interfaces.exception.InvalidQueryException;
import ar.edu.itba.pawgram.interfaces.persistence.PostDao;
import ar.edu.itba.pawgram.model.*;
import ar.edu.itba.pawgram.model.query.OrderCriteria;
import ar.edu.itba.pawgram.model.query.PostSortCriteria;
import ar.edu.itba.pawgram.model.structures.Location;
import ar.edu.itba.pawgram.persistence.querybuilder.PostKeywordQueryBuilder;
import org.apache.commons.lang3.text.StrSubstitutor;
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

    private final EnumMap<PostSortCriteria, PostSortCriteriaClause> postSortCriteriaClauseMap;
    private final EnumMap<OrderCriteria, OrderCriteriaClause> orderCriteriaClauseMap;

    public PostHibernateDao() {
        postSortCriteriaClauseMap = new EnumMap<>(PostSortCriteria.class);
        initProductSortCriteriaClauseMap();

        orderCriteriaClauseMap = new EnumMap<>(OrderCriteria.class);
        initOrderCriteriaClauseMap();
    }

    private void initProductSortCriteriaClauseMap() {
        postSortCriteriaClauseMap.put(PostSortCriteria.ALPHA, new PostSortCriteriaClause("lower(p.title)"));
        postSortCriteriaClauseMap.put(PostSortCriteria.ID, new PostSortCriteriaClause("p.id"));
        postSortCriteriaClauseMap.put(PostSortCriteria.DATE, new PostSortCriteriaClause("p.event_date"));
        postSortCriteriaClauseMap.put(PostSortCriteria.DISTANCE, new PostSortCriteriaClause("haversine_distance(:lat1,:lon1,p.location.latitude,p.location.longitude)"));
    }

    private void initOrderCriteriaClauseMap() {
        orderCriteriaClauseMap.put(OrderCriteria.ASC, new OrderCriteriaClause("ASC"));
        orderCriteriaClauseMap.put(OrderCriteria.DESC, new OrderCriteriaClause("DESC"));
    }

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
    public List<Post> getPlainPostsRange(PostSortCriteria postSortCriteria, OrderCriteria orderCriteria,
                                         int limit, int offset) throws InvalidQueryException {
        final String queryStr = getQuery("select p from Post as p ORDER BY ${order} ${orderCriteria}",postSortCriteria,orderCriteria);
        final TypedQuery<Post> query = em.createQuery(queryStr, Post.class);

        return pagedResult(query, offset, limit);
    }

    @Override
    public List<Post> getPlainPostsRange(Location location, PostSortCriteria postSortCriteria, OrderCriteria orderCriteria,
                                         int limit, int offset) throws InvalidQueryException {
        final String queryStr = getQuery("select p from Post as p ORDER BY ${order} ${orderCriteria}",
                location,postSortCriteria,orderCriteria);
        final TypedQuery<Post> query = em.createQuery(queryStr, Post.class);
        query.setParameter("lat1",location.getLatitude());
        query.setParameter("lon1",location.getLongitude());

        return pagedResult(query, offset, limit);
    }

    @Override
    public List<Post> getPlainPostsByCategoryRange(Category category, PostSortCriteria postSortCriteria, OrderCriteria orderCriteria,
                                                   int limit, int offset) throws InvalidQueryException{
        final String queryStr = getQuery("select p from Post as p WHERE p.category = :category ORDER BY ${order} ${orderCriteria}",
                postSortCriteria,orderCriteria);
        final TypedQuery<Post> query = em.createQuery(queryStr, Post.class);
        query.setParameter("category", category);
        return pagedResult(query, offset, limit);
    }

    @Override
    public List<Post> getPlainPostsByCategoryRange(Location location, Category category, PostSortCriteria postSortCriteria, OrderCriteria orderCriteria,
                                                   int limit, int offset) throws InvalidQueryException{
        final String queryStr = getQuery("select p from Post as p WHERE p.category = :category ORDER BY ${order} ${orderCriteria}",
                location,postSortCriteria,orderCriteria);
        final TypedQuery<Post> query = em.createQuery(queryStr, Post.class);
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
    public List<Post> getPlainPostsRange(Location location, int range, PostSortCriteria postSortCriteria, OrderCriteria orderCriteria,
                                         int limit, int offset) throws InvalidQueryException {
        final String queryStr = getQuery("select p from Post as p WHERE haversine_distance(:lat1,:lon1,p.location.latitude,p.location.longitude) < :range " +
                        " ORDER BY ${order} ${orderCriteria}"
                ,location,postSortCriteria,orderCriteria);
        final TypedQuery<Post> query = em.createQuery( queryStr, Post.class);

        query.setParameter("lat1",location.getLatitude());
        query.setParameter("lon1",location.getLongitude());
        query.setParameter("range",range);

        return pagedResult(query, offset, limit);
    }

    @Override
    public List<Post> getPlainPostsByCategoryRange(Location location, int range, Category category, PostSortCriteria postSortCriteria, OrderCriteria orderCriteria,
                                                   int limit, int offset) throws InvalidQueryException {
        final String queryStr = getQuery("select p from Post as p WHERE p.category = :category AND " +
                " haversine_distance(:lat1,:lon1,p.location.latitude,p.location.longitude) < :range " +
                " ORDER BY ${order} ${orderCriteria}",location,postSortCriteria,orderCriteria);
        final TypedQuery<Post> query = em.createQuery(queryStr, Post.class);

        query.setParameter("category",category);
        query.setParameter("lat1",location.getLatitude());
        query.setParameter("lon1",location.getLongitude());
        query.setParameter("range",range);

        return pagedResult(query, offset, limit);
    }

    @Override
    public List<Post> getPlainPostsByKeywordRange(Set<String> keywords, PostSortCriteria postSortCriteria, OrderCriteria orderCriteria, int limit, int offset) throws InvalidQueryException {
        final Map<String, String> keyWordsRegExp = new HashMap<>();
        final String whereQuery = postKeywordQueryBuilder.buildQuery(keywords, keyWordsRegExp);
        final String queryStr = getQuery("from Post as p where (" + whereQuery + ") ORDER BY ${order} ${orderCriteria}",postSortCriteria,orderCriteria);
        final TypedQuery<Post> query = em.createQuery(queryStr, Post.class);

        for (final Map.Entry<String, String> e : keyWordsRegExp.entrySet())
            query.setParameter(e.getKey(), e.getValue());

        return pagedResult(query, offset, limit);
    }

    @Override
    public List<Post> getPlainPostsByKeywordRange(Set<String> keywords, Location location, PostSortCriteria postSortCriteria, OrderCriteria orderCriteria, int limit, int offset) throws InvalidQueryException {
        final Map<String, String> keyWordsRegExp = new HashMap<>();
        final String whereQuery = postKeywordQueryBuilder.buildQuery(keywords, keyWordsRegExp);
        final String queryStr = getQuery("from Post as p where (" + whereQuery + ") ORDER BY ${order} ${orderCriteria}",
                location, postSortCriteria,orderCriteria);
        final TypedQuery<Post> query = em.createQuery(queryStr, Post.class);

        query.setParameter("lat1",location.getLatitude());
        query.setParameter("lon1",location.getLongitude());

        for (final Map.Entry<String, String> e : keyWordsRegExp.entrySet())
            query.setParameter(e.getKey(), e.getValue());

        return pagedResult(query, offset, limit);
    }

    @Override
    public List<Post> getPlainPostsByKeywordRange(Set<String> keywords, Category category, PostSortCriteria postSortCriteria, OrderCriteria orderCriteria, int limit, int offset) throws InvalidQueryException {
        final Map<String, String> keyWordsRegExp = new HashMap<>();
        final String whereQuery = postKeywordQueryBuilder.buildQuery(keywords, keyWordsRegExp);
        final String queryStr = getQuery("from Post as p where p.category = :category AND (" + whereQuery + ") ORDER BY ${order} ${orderCriteria}",
                postSortCriteria,orderCriteria);

        final TypedQuery<Post> query = em.createQuery(queryStr, Post.class);

        query.setParameter("category", category);
        for (final Map.Entry<String, String> e : keyWordsRegExp.entrySet())
            query.setParameter(e.getKey(), e.getValue());

        return pagedResult(query, offset, limit);
    }

    @Override
    public List<Post> getPlainPostsByKeywordRange(Set<String> keywords, Location location, Category category, PostSortCriteria postSortCriteria, OrderCriteria orderCriteria, int limit, int offset) throws InvalidQueryException {
        final Map<String, String> keyWordsRegExp = new HashMap<>();
        final String whereQuery = postKeywordQueryBuilder.buildQuery(keywords, keyWordsRegExp);
        final String queryStr = getQuery("from Post as p where p.category = :category AND (" + whereQuery + ") ORDER BY ${order} ${orderCriteria}",
                location, postSortCriteria,orderCriteria);
        final TypedQuery<Post> query = em.createQuery(queryStr, Post.class);

        query.setParameter("category", category);
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

    private String getQuery(String notInjectedQuery, PostSortCriteria postSortCriteria, OrderCriteria orderCriteria) throws InvalidQueryException{
        return getQuery(notInjectedQuery,null,postSortCriteria,orderCriteria);
    }

    private String getQuery(String notInjectedQuery, Location location, PostSortCriteria postSortCriteria, OrderCriteria orderCriteria) throws InvalidQueryException {
        //If there is no location :lat1, :lon1 wont be defined, so we throw an exception
        if(location == null && PostSortCriteria.DISTANCE.equals(postSortCriteria)){
            throw new InvalidQueryException();
        }
        String strQuery = postSortCriteriaClauseMap.get(postSortCriteria).injectIntoTemplate(notInjectedQuery);
        return orderCriteriaClauseMap.get(orderCriteria).injectIntoTemplate(strQuery);
    }

    private static class PostSortCriteriaClause {
        private final StrSubstitutor substitutor;

        public PostSortCriteriaClause(final String orderClause) {
            final Map<String, String> valuesMap = new HashMap<>();
            valuesMap.put("order", orderClause);

            substitutor = new StrSubstitutor(valuesMap);
        }

        public String injectIntoTemplate(final String template) {
            return substitutor.replace(template);
        }
    }

    private static class OrderCriteriaClause {
        private final StrSubstitutor substitutor;

        public OrderCriteriaClause(final String orderCriteriaClause) {
            final Map<String, String> valuesMap = new HashMap<>();

            valuesMap.put("orderCriteria", orderCriteriaClause);

            substitutor = new StrSubstitutor(valuesMap);
        }

        public String injectIntoTemplate(final String template) {
            return substitutor.replace(template);
        }

    }
}
