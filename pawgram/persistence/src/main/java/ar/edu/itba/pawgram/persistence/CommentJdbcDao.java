package ar.edu.itba.pawgram.persistence;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import ar.edu.itba.pawgram.interfaces.exception.InvalidCommentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import ar.edu.itba.pawgram.interfaces.persistence.CommentDao;
import ar.edu.itba.pawgram.interfaces.persistence.UserDao;
import ar.edu.itba.pawgram.model.Comment;
import ar.edu.itba.pawgram.persistence.rowmapper.CommentRowMapper;

@Repository
public class CommentJdbcDao implements CommentDao {	
	@Autowired
	private CommentRowMapper commentRowMapper;
	
	@Autowired 
	private UserDao userDao;
	
	private final JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert jdbcInsert;
	
	@Autowired
	public CommentJdbcDao(final DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
					.withTableName("comments")
					.usingGeneratedKeyColumns("commentid");
	}

	@Override
	public List<Comment> getCommentsByPostId(final long id) {
		final List<Comment> comments = jdbcTemplate.query("SELECT * FROM comments, users WHERE comments.userId = users.id AND postId = ? ORDER BY parentId NULLS FIRST, commentDate ASC",
				commentRowMapper, id);
		return comments;	
	}
	
	@Override
	public Comment createComment(final String content, final LocalDateTime date,final long parentId, final long postId, final long userId) throws InvalidCommentException {
		final Map<String, Object> args = argsMap(content, date, postId, userId);
		args.put("parentId", parentId);
		if(!isParentComment(parentId))
			throw new InvalidCommentException();

		final Number commentId = jdbcInsert.executeAndReturnKey(args);

		return new Comment(commentId.intValue(), parentId, userDao.findById(userId), content, date);
	}

	@Override
	public Comment createParentComment(final String content,final LocalDateTime date,final long postId, final long userId) {
		final Map<String, Object> args = argsMap(content, date, postId, userId);

		final Number commentId = jdbcInsert.executeAndReturnKey(args);

		return new Comment(commentId.intValue(), userDao.findById(userId), content, date);
	}


	private boolean isParentComment(long commentId) {
		Integer total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM comments WHERE commentId = ? AND parentId IS NULL", Integer.class,commentId);
		if(total == null)
			return false;
		return (total == 0)? false : true;
	}

	private Map<String, Object> argsMap(final String content, final LocalDateTime date, final long postId, final long userId) {
		final Map<String, Object> args = new HashMap<String, Object>();
		args.put("commentContent", content);
		args.put("commentDate", Timestamp.valueOf(date));
		args.put("userId", userId);
		args.put("postId", postId);
		
		return args;
	}
}