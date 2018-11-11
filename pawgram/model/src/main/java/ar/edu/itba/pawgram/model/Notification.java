package ar.edu.itba.pawgram.model;

import javax.persistence.*;

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

    // Hibernate
    Notification() {
    }

    public Notification(User user, Post post, Comment comment) {
        this.user = notNull(user, "User can not be null");
        this.post = notNull(post, "Post can not be null");
        this.comment = comment;
        this.is_seen = false;
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
}
