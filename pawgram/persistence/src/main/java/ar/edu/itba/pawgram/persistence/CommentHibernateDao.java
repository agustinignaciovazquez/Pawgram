package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.persistence.CommentDao;
import ar.edu.itba.pawgram.model.Comment;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class CommentHibernateDao implements CommentDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Comment> getCommentsByPostId(final long id) {
        final TypedQuery<Comment> query = em.createQuery("from Comment as c where c.commentedPost.id = :postId "
                + "ORDER BY c.parent.id NULLS FIRST, c.commentDate ASC", Comment.class);
        query.setParameter("postId", id);

        return query.getResultList();
    }

    @Override
    public Comment createComment(final String content, final Date date, final Comment parent, final Post post, final User author) {
        final Comment comment = new Comment(parent, author, post, content, date);

        em.persist(comment);

        return comment;
    }

    @Override
    public Comment createParentComment(final String content, final Date date, final Post post, final User author) {
        final Comment comment = new Comment(author, post, content, date);

        em.persist(comment);

        return comment;
    }

    @Override
    public Comment getCommentById(long commentId) {
        return em.find(Comment.class, commentId);
    }
}
