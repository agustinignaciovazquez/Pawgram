package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.persistence.MessageDao;
import ar.edu.itba.pawgram.model.Message;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class MessageHibernateDao implements MessageDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Message> getMessages(User origin, User destination, int limit, int offset) {
        final TypedQuery<Message> query = em.createQuery("select m from Message as m WHERE (m.orig_user.id = :origId AND m.dest_user.id = :destId) " +
                "OR (m.orig_user.id = :destId AND m.dest_user.id = :origId) " +
                " ORDER BY m.messageDate ASC", Message.class);
        query.setParameter("origId",origin.getId());
        query.setParameter("destId",destination.getId());

        return pagedResult(query, offset, limit);
    }

    @Override
    public Message sendMessage(User origin, User destination, String content, Date date) {
        final Message message = new Message(destination,origin,content,date);

        em.persist(message);

        return message;
    }

    @Override
    public long getTotalMessages(User origin, User destination){
        final TypedQuery<Long> query = em.createQuery("select count(*) from Message as m WHERE (m.orig_user.id = :origId AND m.dest_user.id = :destId) " +
                " OR (m.orig_user.id = :destId AND m.dest_user.id = :origId) ", Long.class);
        final Long total = query.getSingleResult();
        return total != null ? total : 0;
    }

    @Override
    public List<User> getMessageUsers(User origin) {
        final TypedQuery<User> query = em.createQuery("select distinct u from User as u, Message as m WHERE ((m.orig_user.id = u.id AND m.dest_user.id = :origId)" +
                "OR ( m.dest_user.id = u.id  AND m.orig_user.id = :origId)) AND u.id != :origId ORDER by u.name ASC",User.class);
        query.setParameter("origId",origin.getId());

        return query.getResultList();
    }

    private List<Message> pagedResult(final TypedQuery<Message> query, final int offset, final int length) {
        query.setFirstResult(offset);
        query.setMaxResults(length);
        return query.getResultList();
    }
}
