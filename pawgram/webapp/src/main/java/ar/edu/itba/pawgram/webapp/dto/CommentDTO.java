package ar.edu.itba.pawgram.webapp.dto;

import ar.edu.itba.pawgram.model.Comment;
import ar.edu.itba.pawgram.model.CommentFamily;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;



public class CommentDTO {
    private long id;
    private Date date;
    private String content;
    private UserDTO author;
    private List<CommentDTO> children;

    public static List<CommentDTO> fromCommentFamilyList(final List<CommentFamily> list, final URI baseUri) {
        final List<CommentDTO> commentFamilies = new ArrayList<CommentDTO>(list.size());
        list.forEach((commentFamily) -> commentFamilies.add(new CommentDTO(commentFamily, baseUri)));

        return commentFamilies;
    }

    private static List<CommentDTO> fromCommentList(final List<Comment> list, final URI baseUri) {
        final List<CommentDTO> comments = new ArrayList<CommentDTO>(list.size());
        list.forEach((comment) -> comments.add(new CommentDTO(comment, baseUri)));

        return comments;
    }

    public CommentDTO() {}

    public CommentDTO(final CommentFamily commentFamily, final URI baseUri) {
        this(commentFamily.getParentComment(), baseUri);
        this.children = fromCommentList(commentFamily.getChildComments(), baseUri);
    }

    public CommentDTO(final Comment comment, final URI baseUri) {
        this.id = comment.getId();
        this.date = comment.getCommentDate();
        this.content = comment.getContent();
        this.author = new UserDTO(comment.getAuthor(), baseUri);
        this.children = Collections.emptyList();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public List<CommentDTO> getChildren() {
        return children;
    }

    public void setChildren(List<CommentDTO> children) {
        this.children = children;
    }
}

