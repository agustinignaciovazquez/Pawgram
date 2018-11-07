package ar.edu.itba.pawgram.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.edu.itba.pawgram.interfaces.exception.InvalidCommentException;
import ar.edu.itba.pawgram.interfaces.service.PostService;
import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.itba.pawgram.interfaces.persistence.CommentDao;
import ar.edu.itba.pawgram.interfaces.service.CommentService;
import ar.edu.itba.pawgram.model.Comment;
import ar.edu.itba.pawgram.model.CommentFamily;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentDao commentDao;

	@Autowired
	private UserService userService;

	@Autowired
	private PostService postService;

	@Override
	public List<CommentFamily> getCommentsByPostId(final long id) {
		List<Comment> comments = commentDao.getCommentsByPostId(id);
		List<CommentFamily> parents = new ArrayList<>();
		int parentIndex = 0;
		int commentIndex = 0;

		while (commentIndex < comments.size()) {
			Comment c = comments.get(commentIndex);

			if (!c.hasParent()) {
				CommentFamily commentFamily = new CommentFamily(c);
				parents.add(commentFamily);
				commentIndex++;
			} else if (c.getParent().equals(parents.get(parentIndex).getParentComment())) {
				parents.get(parentIndex).addChildComment(c);
				commentIndex++;
			} else
				parentIndex++;
		}

		return parents;
	}

	@Transactional
	@Override
	public Comment createComment(final String content, final long parentId, final long postId, final long userId) throws InvalidCommentException {
		final Comment parent = commentDao.getCommentById(parentId);
		final User author = userService.findById(userId);
		final Post post = postService.getPlainPostById(postId);
		if(post == null || author == null || parent == null)
			throw new InvalidCommentException();
		return commentDao.createComment(content, new Date(), parent,post,author);
	}

	@Transactional
	@Override
	public Comment createParentComment(final String content, final long postId, final long userId) throws InvalidCommentException{
		final User author = userService.findById(userId);
		final Post post = postService.getPlainPostById(postId);
		if(post == null || author == null)
			throw new InvalidCommentException();
		return commentDao.createParentComment(content, new Date(), post, author);
	}

}