package ar.edu.itba.pawgram.service;

import ar.edu.itba.pawgram.interfaces.exception.InvalidNotificationException;
import ar.edu.itba.pawgram.interfaces.persistence.NotificationDao;
import ar.edu.itba.pawgram.interfaces.service.NotificationService;
import ar.edu.itba.pawgram.model.Comment;
import ar.edu.itba.pawgram.model.Notification;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    NotificationDao notificationDao;

    @Override
    public List<Notification> getNotifications(User user, boolean include_seen_notifications) {
        return notificationDao.getNotifications(user,include_seen_notifications);
    }

    @Override
    @Transactional
    public Notification createNotification(User user, Post post, Comment comment) {
        return notificationDao.createNotification(user,post,comment,new Date());
    }

    @Override
    public long getTotalNotificationByUser(User user, boolean include_seen_notifications) {
        return notificationDao.getTotalNotificationByUser(user,include_seen_notifications);
    }

    @Override
    @Transactional
    public Set<Notification> createNotificationsForPost(Post post, Comment comment) {
        Set<Notification> notifications = new HashSet<>();
        Set<User> usersNotified = new HashSet<>();
        for(User u: post.getUserSubscriptions()){
            //Do not allow self notifications
            if(!comment.getAuthor().equals(u))
                notifications.add(createNotification(u,post,comment));
                usersNotified.add(u);
        }

        //Create notification to the owner (since it is not possible to subscribe to our own posts)
        if(!usersNotified.contains(post.getOwner()) && comment != null) {
            notifications.add(createNotification(post.getOwner(), post, comment));
            usersNotified.add(post.getOwner());
        }

        //Create notification if there is a comment and its a parent
        if(comment != null && comment.hasParent()){
            User parentAuthor = comment.getParent().getAuthor();
            //Do not allow self notifications
            if(!(comment.getAuthor().equals(parentAuthor))){
                if(!usersNotified.contains(parentAuthor)) {
                    notifications.add(createNotification(parentAuthor, post, comment));
                    usersNotified.add(parentAuthor);
                }
            }
        }
        return notifications;
    }

    @Override
    public Notification getPlainNotificationById(long notificationId) {
        return notificationDao.getPlainNotificationById(notificationId);
    }

    @Override
    public Notification getFullNotificationById(long notificationId) {
        return notificationDao.getFullNotificationById(notificationId);
    }

    @Override
    @Transactional
    public boolean markNotificationAsSeen(long notificationId) throws InvalidNotificationException {
        return notificationDao.markNotificationAsSeen(notificationId);
    }
}
