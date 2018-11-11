package ar.edu.itba.pawgram.service;

import ar.edu.itba.pawgram.interfaces.persistence.NotificationDao;
import ar.edu.itba.pawgram.interfaces.service.NotificationService;
import ar.edu.itba.pawgram.model.Comment;
import ar.edu.itba.pawgram.model.Notification;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return notificationDao.createNotification(user,post,comment);
    }

    @Override
    public long getTotalNotificationByUser(User user, boolean include_seen_notifications) {
        return notificationDao.getTotalNotificationByUser(user,include_seen_notifications);
    }

    @Override
    @Transactional
    public Set<Notification> createNotificationsForPost(Post post, Comment comment) {
        Set<Notification> notifications = new HashSet<>();
        for(User u: post.getUserSubscriptions()){
            notifications.add(createNotification(u,post,comment));
        }
        return notifications;
    }
}
