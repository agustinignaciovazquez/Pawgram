package ar.edu.itba.pawgram.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import ar.edu.itba.pawgram.model.Comment;

public interface CommentDao {
	/**
	 * Creates a {@link Comment} associated with no other comments, inserting it into the database.
	 * @param content - Content of the comment
	 * @param date - The date the comment was posted
	 * @param postId - ID of the {@link Post} this comment belongs to
	 * @param userId - ID of the {@link User} the posted this comment
	 * @return The created {@link Comment}
	 */
	public Comment createParentComment(final String content,final LocalDateTime date,final long postId, final long userId);
	
	/**
	 * Creates a {@link Comment} associated with a parent comment, inserting it into the database.
	 * @param content - Content of the comment
	 * @param date - The date the comment was posted
	 * @param parentId - ID of the {@link Comment} this comment is associated with
	 * @param postId - ID of the {@link Post} this comment belongs to
	 * @param userId - ID of the {@link User} the posted this comment
	 * @return The created {@link Comment}
	 */
	public Comment createComment(final String content, final LocalDateTime date,final long parentId, final long postId, final long userId);
	
	/**
	 * Lists comments of a specific {@link Product} sorted by parent comments first, 
	 * then child comments of the first parent comment and so on.
	 * @param postId - ID of the post the comments refer to.
	 * @return {@link List} of comments associated with the {@link Product}. 
	 * 		   Could be empty if there are no comments associated with the product.
	 */
	public List<Comment> getCommentsByPostId(final long id);
}