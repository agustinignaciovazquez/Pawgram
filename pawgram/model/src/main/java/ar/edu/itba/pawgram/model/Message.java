package ar.edu.itba.pawgram.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messages_messageid_seq")
    @SequenceGenerator(sequenceName = "messages_messageid_seq", name = "messages_messageid_seq", allocationSize = 1)
    @Column(name = "messageid")
    private final long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destId", nullable = false, updatable = false)
    private final User dest_user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "origId", nullable = false, updatable = false)
    private final User orig_user;
    @Column(name = "message", length = 1024, nullable = false)
    private final String message;
    @Temporal(TemporalType.TIMESTAMP)
    private final Date messageDate;

    public Message(long id, User dest_user, User orig_user, String message, Date messageDate) {
        this.id = id;
        this.dest_user = dest_user;
        this.orig_user = orig_user;
        this.message = message;
        this.messageDate = messageDate;
    }

    public Message(User dest_user, User orig_user, String message, Date messageDate){
        this(0,dest_user,orig_user,message,messageDate);
    }

    public long getId() {
        return id;
    }

    public User getDest_user() {
        return dest_user;
    }

    public User getOrig_user() {
        return orig_user;
    }

    public String getMessage() {
        return message;
    }

    public Date getMessageDate() {
        return messageDate;
    }
}
