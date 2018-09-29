package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.exception.PostCreateException;
import ar.edu.itba.pawgram.interfaces.persistence.PostDao;
import ar.edu.itba.pawgram.interfaces.persistence.PostImageDao;
import ar.edu.itba.pawgram.interfaces.service.PostImageService;
import ar.edu.itba.pawgram.model.*;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;
import ar.edu.itba.pawgram.persistence.rowmapper.PlainPostRowMapper;
import ar.edu.itba.pawgram.persistence.rowmapper.PostBuilderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class PostJdbcDao implements PostDao {
    @Autowired
    private PostBuilderRowMapper postBuilderRowMapper;

    @Autowired
    private PlainPostRowMapper plainPostRowMapper;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public PostJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("posts")
                .usingGeneratedKeyColumns("postid");
    }

    @Override
    public Post.PostBuilder createPost(String title, String description, List<byte[]> images, String contact_phone, LocalDateTime event_date,
                                       Category category, Pet pet, boolean is_male, Location location, User owner)  throws PostCreateException {
        final Map<String, Object> args = new HashMap<String, Object>();
        args.put("title", title);
        args.put("description", description);
        args.put("contact_phone", contact_phone);
        args.put("category", category.getLowerName().toUpperCase(Locale.ENGLISH));
        args.put("pet", pet.getLowerName().toUpperCase(Locale.ENGLISH));
        args.put("event_date", Timestamp.valueOf(event_date));
        args.put("is_male", is_male);
        args.put("latitude", location.getLatitude());
        args.put("longitude", location.getLongitude());
        args.put("userId", owner.getId());

        final Number postId = jdbcInsert.executeAndReturnKey(args);

        //byte[] to PostImage FIX

        List<PostImage> imgs = null;
        if ( images != null ) {
            imgs = new ArrayList<>();
            PostImage pi;
            for( byte[] b : images ){
                pi = new PostImage(postId.longValue(), b.toString(), postId.longValue());
                imgs.add(pi);
            }
        }

        return Post.getBuilder(postId.longValue(), title, imgs)
                .category(category).pet(pet)
                .description(description)
                .contact_phone(contact_phone)
                .event_date(event_date)
                .is_male(is_male)
                .location(location)
                .distance(0)
                .user(owner);
    }

    @Override
    public List<PlainPost> getPlainPostsRange(int limit, long offset) {
        return jdbcTemplate.query("SELECT postId, title, category, pet, is_male, " +
                        " 0 as distance" +
                        " FROM posts ORDER BY postId ASC LIMIT ? OFFSET ?",
                plainPostRowMapper,limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsRange(Location location, int limit, long offset) {
        return jdbcTemplate.query("SELECT * FROM (SELECT postId, title, category, pet, is_male, " +
                        " haversine_distance(?,?,latitude,longitude) as distance" +
                        " FROM posts LIMIT ? OFFSET ?) ss ORDER BY distance DESC",
                plainPostRowMapper,location.getLatitude(),location.getLongitude(),limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByCategoryRange(Category category, int limit, long offset) {
        return jdbcTemplate.query("SELECT postId, title, category, pet, is_male,  " +
                        "0 as distance" +
                        " FROM posts WHERE category = ? ORDER BY postId ASC LIMIT ? OFFSET ?",
                plainPostRowMapper,category.getLowerName().toUpperCase(Locale.ENGLISH),limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByCategoryRange(Location location, Category category, int limit, long offset) {
        return jdbcTemplate.query("SELECT * FROM (SELECT postId, title, category, pet, is_male,  " +
                        "haversine_distance(?,?,latitude,longitude) as distance" +
                        " FROM posts WHERE category = ? LIMIT ? OFFSET ?) ss ORDER BY distance DESC",
                plainPostRowMapper,location.getLatitude(),location.getLongitude(),
                category.getLowerName().toUpperCase(Locale.ENGLISH),limit,offset);
    }

    //ERROR: java.sql.SQLSyntaxErrorException: duplicate column name in derived table
    @Override
    public Post.PostBuilder getFullPostById(long postId) {
        List<Post.PostBuilder> l = jdbcTemplate.query("SELECT X.*, 0 as distance "  +
                        " FROM (SELECT * FROM posts,users WHERE posts.userId = users.id) X WHERE postId = ?",
                postBuilderRowMapper,postId);
        return (l.isEmpty())? null: l.get(0);
    }

    @Override
    public Post.PostBuilder getFullPostById(long postId, Location location) {
        List<Post.PostBuilder> l = jdbcTemplate.query("SELECT *," +
                        " haversine_distance(?,?,latitude,longitude) as distance " +
                        " FROM posts,users WHERE posts.userId = users.id AND postId = ?",
                postBuilderRowMapper,location.getLatitude(),location.getLongitude(),postId);
        return (l.isEmpty())? null: l.get(0);
    }

    @Override
    public PlainPost getPlainPostById(long postId) {
        List<PlainPost> l = jdbcTemplate.query("SELECT postId, title, category, pet, is_male, " +
                        " 0 as distance FROM posts WHERE postId = ?",
                plainPostRowMapper,postId);
        return (l.isEmpty())? null: l.get(0);
    }

    @Override
    public boolean deletePostById(long postId) {
        return jdbcTemplate.update("DELETE FROM posts WHERE postId = ?", postId) == 1;
    }

    @Override
    public List<PlainPost> getPlainPostsRange(Location location, int range, int limit, long offset) {
        return jdbcTemplate.query("SELECT * FROM (SELECT postId, title, category, pet, is_male, " +
                        " haversine_distance(?,?,latitude,longitude) as distance" +
                        " FROM posts) ss WHERE distance <= ? ORDER BY distance DESC LIMIT ? OFFSET ?",
                plainPostRowMapper,location.getLatitude(),location.getLongitude(),range,limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByCategoryRange(Location location, int range, Category category, int limit, long offset) {
        return jdbcTemplate.query("SELECT * FROM (SELECT postId, title, category, pet, is_male,  " +
                        "haversine_distance(?,?,latitude,longitude) as distance" +
                        " FROM posts WHERE category = ?) " +
                        "ss WHERE distance <= ? ORDER BY distance DESC LIMIT ? OFFSET ?",
                plainPostRowMapper,location.getLatitude(),location.getLongitude(),category.getLowerName().toUpperCase(Locale.ENGLISH),range, limit,offset);
    }


    @Override
    public List<PlainPost> getPlainPostsByKeywordRange(String keyword, int limit, long offset) {
        return jdbcTemplate.query("SELECT postId, title, category, pet, is_male, " +
                        " 0 as distance" +
                        " FROM posts WHERE lower(title) LIKE lower(?) OR lower(description) LIKE lower(?) ORDER BY postId ASC LIMIT ? OFFSET ?",
                plainPostRowMapper,"%"+keyword+"%", "%"+keyword+"%", limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByKeywordRange(String keyword, Location location, int limit, long offset) {
        return jdbcTemplate.query("SELECT * FROM (SELECT postId, title, category, pet, is_male, " +
                        " haversine_distance(?,?,latitude,longitude) as distance" +
                        " FROM posts WHERE lower(title) LIKE lower(?) OR lower(description) LIKE lower(?) LIMIT ? OFFSET ?) ss ORDER BY distance DESC",
                plainPostRowMapper,location.getLatitude(),location.getLongitude(),"%"+keyword+"%","%"+keyword+"%",limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByKeywordRange(String keyword, Category category, int limit, long offset) {
        return jdbcTemplate.query("SELECT postId, title, category, pet, is_male, " +
                        " 0 as distance" +
                        " FROM posts WHERE category = ? AND (lower(title) LIKE lower(?) OR lower(description) LIKE lower(?)) ORDER BY postId ASC LIMIT ? OFFSET ?",
                plainPostRowMapper,category.getLowerName().toUpperCase(Locale.ENGLISH),"%"+keyword+"%","%"+keyword+"%",limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByKeywordRange(String keyword, Location location, Category category, int limit, long offset) {
        return jdbcTemplate.query("SELECT * FROM (SELECT postId, title, category, pet, is_male, " +
                        " haversine_distance(?,?,latitude,longitude) as distance" +
                        " FROM posts WHERE category = ? AND (lower(title) LIKE lower(?) OR lower(description) LIKE lower(?)) LIMIT ? OFFSET ?) ss ORDER BY distance DESC",
                plainPostRowMapper,location.getLatitude(),location.getLongitude(),
                category.getLowerName().toUpperCase(Locale.ENGLISH),"%"+keyword+"%","%"+keyword+"%",limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByUserIdRange(long userId, int limit, long offset) {
        return jdbcTemplate.query("SELECT postId, title, category, pet, is_male, " +
                        " 0 as distance" +
                        " FROM posts WHERE userId = ? ORDER BY postId ASC LIMIT ? OFFSET ?",
                plainPostRowMapper,userId,limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByUserIdRange(long userId, Location location, int limit, long offset) {
        return jdbcTemplate.query("SELECT * FROM (SELECT postId, title, category, pet, is_male, " +
                        " haversine_distance(?,?,latitude,longitude) as distance" +
                        " FROM posts WHERE userId = ? DESC LIMIT ? OFFSET ?) ss ORDER BY distance",
                plainPostRowMapper,location.getLatitude(),location.getLongitude(),userId,limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByUserIdRange(long userId, Category category, int limit, long offset) {
        return jdbcTemplate.query("SELECT postId, title, category, pet, is_male, " +
                        " 0 as distance" +
                        " FROM posts WHERE userId = ? AND category = ? ORDER BY postId ASC LIMIT ? OFFSET ?",
                plainPostRowMapper,userId,category.getLowerName().toUpperCase(Locale.ENGLISH),limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByUserIdRange(long userId, Location location, Category category, int limit, long offset) {
        return jdbcTemplate.query("SELECT * FROM(SELECT postId, title, category, pet, is_male, " +
                        " haversine_distance(?,?,latitude,longitude) as distance" +
                        " FROM posts WHERE userId = ? AND category = ? DESC LIMIT ? OFFSET ?) ss ORDER BY distance",
                plainPostRowMapper,location.getLatitude(),location.getLongitude(),userId,category.getLowerName().toUpperCase(Locale.ENGLISH),limit,offset);
    }

    @Override
    public long getTotalPosts() {
        Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts", Long.class);
        return total != null ? total : 0;
    }

    @Override
    public long getTotalPostsByCategory(Category category) {
        Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts WHERE category = ?",
                Long.class, category.getLowerName().toUpperCase(Locale.ENGLISH));
        return total != null ? total : 0;
    }

    @Override
    public long getTotalPosts(Location location, int range) {
        Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM (SELECT haversine_distance(?,?,latitude,longitude) as distance" +
                " FROM posts ) ss WHERE distance <= ?", Long.class,location.getLatitude(),location.getLongitude(),range);
        return total != null ? total : 0;
    }

    @Override
    public long getTotalPostsByCategory(Location location, int range, Category category) {
        Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM (SELECT haversine_distance(?,?,latitude,longitude) as distance" +
                        " FROM posts WHERE category = ?) ss WHERE distance <= ?",
                Long.class, location.getLatitude(),location.getLongitude(), category.getLowerName().toUpperCase(Locale.ENGLISH),range);
        return total != null ? total : 0;
    }

    @Override
    public long getTotalPostsByKeyword(String keyword) {
        Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts WHERE lower(title) LIKE lower(?) OR lower(description) LIKE lower(?)",
                Long.class,"%"+keyword+"%","%"+keyword+"%");
        return total != null ? total : 0;
    }

    @Override
    public long getTotalPostsByKeyword(String keyword, Category category) {
        Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts WHERE category = ? AND (lower(title) LIKE lower(?) OR lower(description) LIKE lower(?))",
                Long.class, category.getLowerName().toUpperCase(Locale.ENGLISH),"%"+keyword+"%","%"+keyword+"%");
        return total != null ? total : 0;
    }

    @Override
    public long getTotalPostsByUserId(long userId) {
        Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts WHERE userId = ?", Long.class,userId);
        return total != null ? total : 0;
    }

    @Override
    public long getTotalPostsByUserId(long userId, Category category) {
        Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts WHERE userId = ? AND category = ?",
                Long.class,userId,category.getLowerName().toUpperCase(Locale.ENGLISH));
        return total != null ? total : 0;
    }
}
