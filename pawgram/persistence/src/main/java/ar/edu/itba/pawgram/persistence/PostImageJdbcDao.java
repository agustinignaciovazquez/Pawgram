package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.persistence.PostImageDao;
import ar.edu.itba.pawgram.model.PostImage;
import ar.edu.itba.pawgram.persistence.rowmapper.PostImageRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostImageJdbcDao implements PostImageDao {
    @Autowired
    private PostImageRowMapper postImageRowMapper;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public PostImageJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("postImages")
                .usingGeneratedKeyColumns("postimageid");
    }
    @Override
    public PostImage createPostImage(long postId, String url) {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("postId", postId);
        args.put("url", url);

        final Number postImageId = jdbcInsert.execute(args);
        return new PostImage(postImageId.longValue(),url, postId);
    }

    @Override
    public List<PostImage> getImagesIdByPostId(long postId) {
        return jdbcTemplate.query("SELECT postImageId, postId, url FROM postImages " +
                "ORDER BY postImageId DESC", postImageRowMapper);
    }
}
