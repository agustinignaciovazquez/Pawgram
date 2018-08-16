package ar.edu.itba.pawgram.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.itba.pawgram.interfaces.CommentDao;
import ar.edu.itba.pawgram.interfaces.CommentService;
import ar.edu.itba.pawgram.model.Comment;
import ar.edu.itba.pawgram.model.CommentFamily;

public class CommentServiceImpl implements CommentService{
	@Autowired
	private CommentDao commentDao;
	
	@Override
	public Comment createParentComment(String content, long postId, long userId) {
		return commentDao.createParentComment(content, LocalDateTime.now(), postId, userId);
	}

	@Override
	public Comment createComment(String content, long parentId, long postId, long userId) {
		return commentDao.createComment(content, LocalDateTime.now(), parentId, postId, userId);
	}

	@Override
	public List<CommentFamily> getCommentsByPostId(long id) {
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
			}
			else if (c.getParentId() == parents.get(parentIndex).getParentComment().getId()) {
				parents.get(parentIndex).addChildComment(c);
				commentIndex++;
			}
			else
				parentIndex++;	
		}
				
		return parents;
	}

}
