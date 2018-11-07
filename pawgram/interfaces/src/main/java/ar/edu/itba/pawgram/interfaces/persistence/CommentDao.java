package ar.edu.itba.pawgram.interfaces.persistence;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import ar.edu.itba.pawgram.interfaces.exception.InvalidCommentException;
import ar.edu.itba.pawgram.model.Comment;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;

public interface CommentDao {
	/**
	 * Creates a {@link Comment} associated with no other comments, inserting it into the database.
	 * @param content - Content of the comment
	 * @param date - The date the comment was posted
	 * @param post - {@link Post} this comment belongs to
	 * @param author - {@link User}  posted this comment
	 * @return The created {@link Comment}
	 */
	public Comment createParentComment(final String content, final Date date, final Post post, final User author);

	
	/**
	 * Creates a {@link Comment} associated with a parent comment, inserting it into the database.
	 * @param content - Content of the comment
	 * @param date - The date the comment was posted
	 * @param parent - {@link Comment} parent comment associated with
	 * @param post - {@link Post} this comment belongs to
	 * @param author - {@link User}  posted this comment
	 * @return The created {@link Comment}
	 */
	public Comment createComment(final String content, final Date date, final Comment parent, final Post post, final User author);
	
	/**
	 * Lists comments of a specific {@link Post} sorted by parent comments first, 
	 * then child comments of the first parent comment and so on.
	 * @param id - ID of the post the comments refer to.
	 * @return {@link List} of comments associated with the {@link Post}. 
	 * 		   Could be empty if there are no comments associated with the post.
	 */
	public List<Comment> getCommentsByPostId(final long id);

	/**
	 * Retrieves a {@link Comment} given it's ID.
	 *
	 * @param commentId
	 *            - ID of the comment to retrieve
	 * @return The comment with the corresponding ID or null if it doesn't exist
	 */
	public Comment getCommentById(long commentId);
}
