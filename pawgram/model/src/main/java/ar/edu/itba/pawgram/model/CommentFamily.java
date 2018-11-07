package ar.edu.itba.pawgram.model;
import static org.apache.commons.lang3.Validate.notNull;
import static org.apache.commons.lang3.Validate.isTrue;

import java.util.Collections;
import java.util.List;

import ar.edu.itba.pawgram.model.structures.ParentNode;


public class CommentFamily {

	private final ParentNode<Comment> parentNode;

	public CommentFamily(final Comment parentComment) {
		notNull(parentComment, "Parent comment cannot be null");

		this.parentNode = new ParentNode<Comment>(parentComment);
	}

	public Comment getParentComment() {
		return parentNode.getParent();
	}

	public List<Comment> getChildComments() {
		return Collections.unmodifiableList(parentNode.getChildren());
	}

	public void addChildComment(final Comment child) {
		isTrue(child.getParent().equals(parentNode.getParent()), "Child's parent: %s should be equals to familiy's parent: %s", child.getParent(), parentNode.getParent());
		parentNode.addChild(child);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof CommentFamily))
			return false;

		CommentFamily other = (CommentFamily) obj;

		return other.getParentComment().equals(getParentComment()) && other.parentNode.equals(parentNode);
	}

	@Override
	public int hashCode() {
		return parentNode.hashCode();
	}

	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder(parentNode.getParent().toString());
		for (Comment childComment : parentNode.getChildren())
			stringBuilder.append('\t').append(childComment.toString()).append('\n');
		return stringBuilder.toString();
	}
}