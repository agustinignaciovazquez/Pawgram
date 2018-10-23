package ar.edu.itba.pawgram.persistence.rowmapper;

import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.PostImage;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PostImageRowMapper implements RowMapper<PostImage> {
    public PostImage mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return new PostImage(rs.getLong("postImageId"),rs.getString("url"),rs.getLong("postId"));
    }
}
