package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.persistence.MessageDao;
import ar.edu.itba.pawgram.model.Message;
import ar.edu.itba.pawgram.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public class MessageHibernateDao implements MessageDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Message> getMessages(User origin, User destination) {
        final TypedQuery<Message> query = em.createQuery("select m from Message as m WHERE (m.orig_user.id = :origId AND m.dest_user.id = :destId) " +
                "OR (m.orig_user.id = :destId AND m.dest_user.id = :origId) " +
                " ORDER BY m.messageDate ASC", Message.class);
        query.setParameter("origId",origin.getId());
        query.setParameter("destId",destination.getId());

        final List<Message> l = query.getResultList();
        return l;
    }

    @Override
    public Message sendMessage(User origin, User destination, String content, Date date) {
        final Message message = new Message(destination,origin,content,date);

        em.persist(message);

        return message;
    }
    //TODO this complicated query
    @Override
    public List<User> getMessageUsers(User origin) {
        return null;
    }
}
