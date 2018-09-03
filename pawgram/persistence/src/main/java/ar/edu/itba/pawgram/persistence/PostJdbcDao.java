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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
                .usingGeneratedKeyColumns("postId");
    }

    @Override
    public Post.PostBuilder createPost(String title, String description, List<byte[]> raw_images, String contact_phone, LocalDateTime event_date,
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

        return Post.getBuilder(postId.longValue(), title,null)
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
        return jdbcTemplate.query("SELECT postId, title, category, img_url, pet," +
                        " 0 as distance" +
                        " FROM posts ORDER BY postId ASC LIMIT ? OFFSET ?",
                plainPostRowMapper,limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsRange(Location location, int limit, long offset) {
        return jdbcTemplate.query("SELECT postId, title, category, img_url, pet," +
                        " haversine_distance(?,?,latitude,longitude) as distance" +
                        " FROM posts ORDER BY distance DESC LIMIT ? OFFSET ?",
                plainPostRowMapper,location.getLatitude(),location.getLongitude(),limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByCategoryRange(Category category, int limit, long offset) {
        return jdbcTemplate.query("SELECT postId, title, category, img_url, pet, " +
                        "0 as distance" +
                        " FROM posts WHERE category = ? ORDER BY postId ASC LIMIT ? OFFSET ?",
                plainPostRowMapper,category.getLowerName().toUpperCase(Locale.ENGLISH),limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByCategoryRange(Location location, Category category, int limit, long offset) {
        return jdbcTemplate.query("SELECT postId, title, category, img_url, pet, " +
                        "haversine_distance(?,?,latitude,longitude) as distance" +
                        " FROM posts WHERE category = ? ORDER BY distance DESC LIMIT ? OFFSET ?",
                plainPostRowMapper,location.getLatitude(),location.getLongitude(),
                category.getLowerName().toUpperCase(Locale.ENGLISH),limit,offset);
    }

    @Override
    public Post.PostBuilder getFullPostById(long postId) {
        List<Post.PostBuilder> l = jdbcTemplate.query("SELECT *," +
                        " 0 as distance" +
                        "FROM posts,users WHERE posts.userId = users.id AND postId = ?",
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
        List<PlainPost> l = jdbcTemplate.query("SELECT postId, title, category, img_url, pet," +
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
        return jdbcTemplate.query("SELECT postId, title, category, img_url, pet," +
                        " haversine_distance(?,?,latitude,longitude) as distance" +
                        " FROM posts WHERE distance <= ? ORDER BY distance DESC LIMIT ? OFFSET ?",
                plainPostRowMapper,location.getLatitude(),location.getLongitude(),range,limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByCategoryRange(Location location, int range, Category category, int limit, long offset) {
        return jdbcTemplate.query("SELECT postId, title, category, img_url, pet, " +
                        "haversine_distance(?,?,latitude,longitude) as distance" +
                        " FROM posts WHERE distance <= ? AND category = ? ORDER BY distance DESC LIMIT ? OFFSET ?",
                plainPostRowMapper,location.getLatitude(),location.getLongitude(),
                range,category.getLowerName().toUpperCase(Locale.ENGLISH),limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByKeywordRange(String keyword, int limit, long offset) {
        return jdbcTemplate.query("SELECT postId, title, category, img_url, pet," +
                        " 0 as distance" +
                        " FROM posts WHERE title LIKE %?% ORDER BY postId ASC LIMIT ? OFFSET ?",
                plainPostRowMapper,keyword,limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByKeywordRange(String keyword, Location location, int limit, long offset) {
        return jdbcTemplate.query("SELECT postId, title, category, img_url, pet," +
                        " haversine_distance(?,?,latitude,longitude) as distance" +
                        " FROM posts WHERE title LIKE %?% ORDER BY distance DESC LIMIT ? OFFSET ?",
                plainPostRowMapper,location.getLatitude(),location.getLongitude(),keyword,limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByKeywordRange(String keyword, Category category, int limit, long offset) {
        return jdbcTemplate.query("SELECT postId, title, category, img_url, pet," +
                        " 0 as distance" +
                        " FROM posts WHERE category = ? AND title LIKE %?% ORDER BY postId ASC LIMIT ? OFFSET ?",
                plainPostRowMapper,category.getLowerName().toUpperCase(Locale.ENGLISH),keyword,limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByKeywordRange(String keyword, Location location, Category category, int limit, long offset) {
        return jdbcTemplate.query("SELECT postId, title, category, img_url, pet," +
                        " haversine_distance(?,?,latitude,longitude) as distance" +
                        " FROM posts WHERE category = ? AND title LIKE %?% ORDER BY distance DESC LIMIT ? OFFSET ?",
                plainPostRowMapper,location.getLatitude(),location.getLongitude(),category.getLowerName().toUpperCase(Locale.ENGLISH),keyword,limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByUserIdRange(long userId, int limit, long offset) {
        return jdbcTemplate.query("SELECT postId, title, category, img_url, pet," +
                        " 0 as distance" +
                        " FROM posts WHERE userId = ? ORDER BY postId ASC LIMIT ? OFFSET ?",
                plainPostRowMapper,userId,limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByUserIdRange(long userId, Location location, int limit, long offset) {
        return jdbcTemplate.query("SELECT postId, title, category, img_url, pet," +
                        " haversine_distance(?,?,latitude,longitude) as distance" +
                        " FROM posts WHERE userId = ? ORDER BY distance DESC LIMIT ? OFFSET ?",
                plainPostRowMapper,location.getLatitude(),location.getLongitude(),userId,limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByUserIdRange(long userId, Category category, int limit, long offset) {
        return jdbcTemplate.query("SELECT postId, title, category, img_url, pet," +
                        " 0 as distance" +
                        " FROM posts WHERE userId = ? AND category = ? ORDER BY postId ASC LIMIT ? OFFSET ?",
                plainPostRowMapper,userId,category.getLowerName().toUpperCase(Locale.ENGLISH),limit,offset);
    }

    @Override
    public List<PlainPost> getPlainPostsByUserIdRange(long userId, Location location, Category category, int limit, long offset) {
        return jdbcTemplate.query("SELECT postId, title, category, img_url, pet," +
                        " haversine_distance(?,?,latitude,longitude) as distance" +
                        " FROM posts WHERE userId = ? AND category = ? ORDER BY distance DESC LIMIT ? OFFSET ?",
                plainPostRowMapper,location.getLatitude(),location.getLongitude(),userId,category.getLowerName().toUpperCase(Locale.ENGLISH),limit,offset);
    }

    @Override
    public long getTotalPosts() {
        Integer total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts", Integer.class);
        return total != null ? total : 0;
    }

    @Override
    public long getTotalPostsByCategory(Category category) {
        Integer total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts WHERE category = ?",
                Integer.class, category.getLowerName().toUpperCase(Locale.ENGLISH));
        return total != null ? total : 0;
    }

    @Override
    public long getTotalPosts(Location location, int range) {
        Integer total = jdbcTemplate.queryForObject("SELECT COUNT(*), haversine_distance(?,?,latitude,longitude) as distance" +
                " FROM posts WHERE distance <= ?", Integer.class,location.getLatitude(),location.getLongitude(),range);
        return total != null ? total : 0;
    }

    @Override
    public long getTotalPostsByCategory(Location location, int range, Category category) {
        Integer total = jdbcTemplate.queryForObject("SELECT COUNT(*), haversine_distance(?,?,latitude,longitude) as distance" +
                        " FROM posts WHERE category = ? AND distance <= ?", Integer.class,location.getLatitude(),location.getLongitude(),
                category.getLowerName().toUpperCase(Locale.ENGLISH),range);
        return total != null ? total : 0;
    }

    @Override
    public long getTotalPostsByKeyword(String keyword) {
        Integer total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts WHERE title LIKE %?%", Integer.class,keyword);
        return total != null ? total : 0;
    }

    @Override
    public long getTotalPostsByKeyword(String keyword, Category category) {
        Integer total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts WHERE category = ? AND title LIKE %?%", Integer.class,
                category.getLowerName().toUpperCase(Locale.ENGLISH),keyword);
        return total != null ? total : 0;
    }

    @Override
    public long getTotalPostsByUserId(long userId) {
        Integer total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts WHERE userId = ?", Integer.class,userId);
        return total != null ? total : 0;
    }

    @Override
    public long getTotalPostsByUserId(long userId, Category category) {
        Integer total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts WHERE userId = ? AND category = ?",
                Integer.class,userId,category.getLowerName().toUpperCase(Locale.ENGLISH));
        return total != null ? total : 0;
    }
}
