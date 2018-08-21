package ar.edu.itba.pawgram.persistence.rowmapper;

import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PlainPostRowMapper implements RowMapper<PlainPost> {
    public PlainPost mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return Post.getBuilder(rs.getLong("postId"), rs.getString("title"), rs.getString("img_url"))
                .category(rs.getString("category")).pet(rs.getString("pet")).distance(rs.getInt("distance"))
                .build();
    }
}
