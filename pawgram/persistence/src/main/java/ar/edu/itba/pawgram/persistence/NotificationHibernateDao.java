package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.exception.InvalidNotificationException;
import ar.edu.itba.pawgram.interfaces.persistence.NotificationDao;
import ar.edu.itba.pawgram.model.Comment;
import ar.edu.itba.pawgram.model.Notification;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class NotificationHibernateDao implements NotificationDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Notification> getNotifications(User user, boolean include_seen_notifications) {
        String queryStr = "select n from Notification as n WHERE n.user.id = :userId ORDER BY n.id DESC";
        if(include_seen_notifications == false)
            queryStr = "select n from Notification as n WHERE n.user.id = :userId AND n.is_seen = FALSE ORDER BY n.id DESC";

        final TypedQuery<Notification> query = em.createQuery(queryStr, Notification.class);
        query.setParameter("userId", user.getId());

        return query.getResultList();
    }

    @Override
    public Notification createNotification(User user, Post post, Comment comment, Date date) {
        final Notification notification = new Notification(user,post,comment,date);
        em.persist(notification);
        return notification;
    }

    @Override
    public long getTotalNotificationByUser(User user, boolean include_seen_notifications) {
        String queryStr = "select count(*) from Notification as n WHERE n.user.id = :userId";
        if(include_seen_notifications == false)
            queryStr = "select count(*) from Notification as n WHERE n.user.id = :userId AND n.is_seen = FALSE";

        final TypedQuery<Long> query = em.createQuery(queryStr, Long.class);
        query.setParameter("userId", user.getId());

        final Long total = query.getSingleResult();
        return total != null ? total : 0;
    }

    @Override
    public Notification getPlainNotificationById(long notificationId) {
        return em.find(Notification.class,notificationId);
    }

    @Override
    public boolean markNotificationAsSeen(long notificationId) throws InvalidNotificationException {
        final Notification notification = getPlainNotificationById(notificationId);
        if(notification == null)
            throw new InvalidNotificationException();

        notification.setIs_seen(true);
        return (notification.isIs_seen() == true);
    }
}
