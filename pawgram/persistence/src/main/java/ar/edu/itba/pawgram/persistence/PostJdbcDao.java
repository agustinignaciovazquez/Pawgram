package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.PostDao;
import ar.edu.itba.pawgram.model.*;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;
import ar.edu.itba.pawgram.persistence.rowmapper.PlainPostRowMapper;
import ar.edu.itba.pawgram.persistence.rowmapper.PostBuilderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

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
    public Post.PostBuilder createPost(String title, String description, String img_url, String contact_phone, LocalDateTime event_date,
                                       Category category, Pet pet, boolean is_male, Location location, long ownerId) {
        return null;
    }

    @Override
    public List<PlainPost> getPlainPosts(Location location, int range) {
        return null;
    }

    @Override
    public List<PlainPost> getPlainPostsByCategory(Location location, int range, Category category) {
        return null;
    }

    @Override
    public List<PlainPost> getPlainPostsByKeyword(String keyword, Location location) {
        return null;
    }

    @Override
    public List<PlainPost> getPlainPostsByUserId(long userId) {
        return null;
    }

    @Override
    public Post.PostBuilder getFullPostById(long postId) {
        return null;
    }

    @Override
    public PlainPost getPlainProductById(long postId) {
        return null;
    }

    @Override
    public boolean deletePostById(long postId, User user) {
        return false;
    }

    @Override
    public long getTotalPosts() {
        return 0;
    }

    @Override
    public long getTotalPostsByCategory(Category category) {
        return 0;
    }

    @Override
    public long getTotalPosts(Location location, int range) {
        return 0;
    }

    @Override
    public long getTotalPostsByCategory(Location location, int range, Category category) {
        return 0;
    }
}
