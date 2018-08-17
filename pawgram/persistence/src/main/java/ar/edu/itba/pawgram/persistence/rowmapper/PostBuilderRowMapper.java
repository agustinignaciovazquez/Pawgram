package ar.edu.itba.pawgram.persistence.rowmapper;

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

    public Post.PostBuilder mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return Post.getBuilder(rs.getLong("postId"), rs.getString("title"), rs.getString("img_url"))
                .category(rs.getString("category")).pet(rs.getString("pet"))
                .description(rs.getString("description"))
                .contact_phone(rs.getString("contact_phone"))
                .event_date(rs.getTimestamp("event_date").toLocalDateTime())
                .is_male(rs.getBoolean("is_male"))
                .location(new Location(rs.getDouble("longitude"),rs.getDouble("latitude")))
                .user(userRowMapper.mapRow(rs, rowNum));
    }
}
