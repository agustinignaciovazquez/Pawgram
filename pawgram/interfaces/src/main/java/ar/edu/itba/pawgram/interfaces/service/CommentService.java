package ar.edu.itba.pawgram.interfaces.service;

import java.util.List;

import ar.edu.itba.pawgram.interfaces.exception.InvalidCommentException;
import ar.edu.itba.pawgram.model.Comment;
import ar.edu.itba.pawgram.model.CommentFamily;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;

public interface CommentService {
	
	/**
	 * Creates a {@link Comment} associated with no other comments.
	 * @param content - Content of the comment
	 * @param postId - ID of the {@link Post} this comment belongs to
	 * @param userId - ID of the {@link User} the posted this comment
	 * @return The created {@link Comment}
	 */
	public Comment createParentComment(final String content, final long postId, final long userId) throws InvalidCommentException;
	
	/**
	 * Creates a {@link Comment} associated with a parent comment.
	 * @param content - Content of the comment
	 * @param parentId - ID of the {@link Comment} this comment is associated with
	 * @param postId - ID of the {@link Post} this comment belongs to
	 * @param userId - ID of the {@link User} the posted this comment
	 * @return The created {@link Comment}
	 */
	public Comment createComment(final String content, final long parentId, final long postId, final long userId) throws InvalidCommentException;
	
	/**
	 * Retrieves a {@link List} of parent comments sorted by post date and associated with it's corresponding child comments.
	 * @param id - ID of the post the comments belong to
	 * @return List of parent comments associated with their child comments
	 */
	public List<CommentFamily> getCommentsByPostId(final long id);
}
