package ar.edu.itba.pawgram.webapp.dto;

import ar.edu.itba.pawgram.model.Notification;
import ar.edu.itba.pawgram.model.SearchZone;
import ar.edu.itba.pawgram.model.User;

import javax.swing.text.html.Option;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class NotificationListDTO {
    private List<NotificationDTO> notifications;
    private long totalCount;
    private long count;

    public NotificationListDTO() {}

    public NotificationListDTO(final List<Notification> notifications, long totalCount, final URI baseUri, Optional<User> loggedUser) {
        this.setTotalCount(totalCount);
        this.notifications = new LinkedList<>();
        this.setCount(notifications.size());

        for (Notification n : notifications)
            this.notifications.add(new NotificationDTO(n,baseUri,loggedUser));
    }

    public List<NotificationDTO> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationDTO> notifications) {
        this.notifications = notifications;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
