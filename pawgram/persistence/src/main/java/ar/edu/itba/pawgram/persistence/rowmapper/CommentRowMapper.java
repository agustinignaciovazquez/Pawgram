package ar.edu.itba.pawgram.persistence.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ar.edu.itba.pawgram.model.Comment;
import ar.edu.itba.pawgram.model.User;

@Component
public class CommentRowMapper implements RowMapper<Comment> {
	
	@Autowired
	private UserRowMapper userRowMapper;
	
	public Comment mapRow(final ResultSet rs, final int rowNum) throws SQLException {		
		final User user = userRowMapper.mapRow(rs, rowNum);
		
		final long parentId = rs.getLong("parentId");
		if (rs.wasNull())
			return new Comment(rs.getLong("commentId"), user, rs.getString("commentContent"), rs.getTimestamp("commentDate").toLocalDateTime());		
		
		return new Comment(rs.getLong("commentId"), parentId, user, 
				rs.getString("commentContent"), rs.getTimestamp("commentDate").toLocalDateTime());
	}
}
