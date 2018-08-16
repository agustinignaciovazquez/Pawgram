package ar.edu.itba.pawgram.model;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;
public class Comment {
	private static final int NO_PARENT_ID = -1;
	
	private final long id;
	private final long parentId;
	private final String content;
	private final LocalDateTime commentDate;
	private final User author;
	
	public Comment(long id, User author, String content, LocalDateTime date) {
		this(id, NO_PARENT_ID, author, content, date, true);
	}
	
	public Comment(long id, long parentId, User author, String content, LocalDateTime date) {
		this(id, parentId, author, content, date, false);
	}
	
	private Comment(long id, long parentId, User author, String content, LocalDateTime date, boolean isParentId) {
		isTrue(id >= 0, "Comment ID must be non negative: %d", id);
		isTrue(parentId >= 0 || (parentId == NO_PARENT_ID && isParentId), "Parent comment ID must be non negative: %d", parentId);
		
		this.id = id;
		this.parentId = parentId;
		this.author = notNull(author, "Author of the comment cannot be null");
		this.content = notBlank(content, "Content must have at least one non blank character");
		this.commentDate = notNull(date, "Comment date cannot be null");		
	}
	
	public long getId() {
		return id;
	}
	
	public long getParentId() {
		if (!hasParent())
			throw new NoSuchElementException("Root comment has no parent");
		return parentId;
	}
	
	public String getContent() {
		return content;
	}
	
	public User getAuthor() {
		return author;
	}
	
	public LocalDateTime getCommentDate() {
		return commentDate;
	}
	
	public boolean hasParent() {
		return this.parentId != NO_PARENT_ID;
	}
	
	@Override
	public int hashCode() {
		return (int)id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Comment))
			return false;
		
		Comment other = (Comment) obj;
		return getId() == other.getId();
	}

	@Override
	public String toString() {
		return content;
	}
}
