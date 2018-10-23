package ar.edu.itba.pawgram.persistence.rowmapper;

import ar.edu.itba.pawgram.interfaces.persistence.PostImageDao;
import ar.edu.itba.pawgram.model.Location;
import ar.edu.itba.pawgram.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PostBuilderRowMapper implements RowMapper<Post.PostBuilder> {

    @Autowired
    private UserRowMapper userRowMapper;
    @Autowired
    private PostImageDao postImageDao;

    public Post.PostBuilder mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final long postId = rs.getLong("postId");
        return Post.getBuilder(postId, rs.getString("title"), postImageDao.getImagesIdByPostId(postId))
                .category(rs.getString("category")).pet(rs.getString("pet"))
                .description(rs.getString("description"))
                .contact_phone(rs.getString("contact_phone"))
                .event_date(rs.getTimestamp("event_date").toLocalDateTime())
                .is_male(rs.getBoolean("is_male"))
                .location(new Location(rs.getDouble("longitude"),rs.getDouble("latitude")))
                .distance(rs.getInt("distance"))
                .user(userRowMapper.mapRow(rs, rowNum));
    }
}
