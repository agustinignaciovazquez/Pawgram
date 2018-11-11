package ar.edu.itba.pawgram.model;

import javax.persistence.*;

import java.util.Date;

import static org.apache.commons.lang3.Validate.notNull;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notifications_notificationid_seq")
    @SequenceGenerator(sequenceName = "notifications_notificationid_seq", name = "notifications_notificationid_seq", allocationSize = 1)
    @Column(name = "notificationid")
    private long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userid")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "postid", nullable = false)
    private Post post;
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "commentid", nullable = true)
    private Comment comment;
    @Column(nullable = false)
    private boolean is_seen;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date;

    // Hibernate
    Notification() {
    }

    public Notification(final User user,final Post post,final Comment comment,final Date date) {
        this.user = notNull(user, "User can not be null");
        this.post = notNull(post, "Post can not be null");
        this.comment = comment;
        this.is_seen = false;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Post getPost() {
        return post;
    }

    public Comment getComment() {
        return comment;
    }

    public boolean isIs_seen() {
        return is_seen;
    }

    public void setIs_seen(boolean is_seen) {
        this.is_seen = is_seen;
    }

    public Date getDate() {
        return date;
    }
}
