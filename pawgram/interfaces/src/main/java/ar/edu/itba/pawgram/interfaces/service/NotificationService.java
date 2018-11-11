package ar.edu.itba.pawgram.interfaces.service;

import ar.edu.itba.pawgram.model.Comment;
import ar.edu.itba.pawgram.model.Notification;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;

import java.util.List;
import java.util.Set;

public interface NotificationService {
    /**
     * Lists {@link Notification} of specific {@link User} origin and destination sorted by date.
     * @param user - user which notifications retrieve.
     * @param include_seen_notifications - if true all notifications will be retrieved
     * @return {@link List} of messages associated with the two {@link User}.
     * 		   Could be empty if there are no messages between two users
     */
    public List<Notification> getNotifications(final User user, final boolean include_seen_notifications);

    /**
     * Creates a new {@link Notification} for a specific {@link User}
     * @param user - user to create notification for.
     * @param post - post associated with notification
     * @param comment - comment associated with notification (could be null)
     * @return {@link Notification} created
     */
    public Notification createNotification(final User user, final Post post, final Comment comment);

    /**
     * Counts total of {@link Notification} of a specific {@link User}.
     * @param user - user which notifications total retrieve.
     * @param include_seen_notifications - if true all notifications will be retrieved
     * @return {@link long} with the total of notifications
     */
    public long getTotalNotificationByUser(final User user, final boolean include_seen_notifications);

    /**
     * Creates a new {@link Notification} for a set of {@link User}
     * @param users - users to create notification for.
     * @param post - post associated with notification
     * @param comment - comment associated with notification (could be null)
     * @return {@link List} of {@link Notification} created
     */
    public Set<Notification> createNotificationsForPost(Post post, Comment comment);
}
