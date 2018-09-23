package ar.edu.itba.pawgram.persistence.rowmapper;

import ar.edu.itba.pawgram.interfaces.persistence.PostImageDao;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PlainPostRowMapper implements RowMapper<PlainPost> {
    @Autowired
    private PostImageDao postImageDao;
    public PlainPost mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final long postId = rs.getLong("postId");
        return Post.getBuilder(postId, rs.getString("title"), postImageDao.getImagesIdByPostId(postId))
                .category(rs.getString("category")).pet(rs.getString("pet")).distance(rs.getInt("distance"))
                .is_male(rs.getBoolean("is_male")).build();
    }
}
