package ar.edu.itba.pawgram.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messages_messageid_seq")
    @SequenceGenerator(sequenceName = "messages_messageid_seq", name = "messages_messageid_seq", allocationSize = 1)
    @Column(name = "messageid")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destId", nullable = false, updatable = false)
    private User dest_user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "origId", nullable = false, updatable = false)
    private User orig_user;

    @Column(name = "message", length = 1024, nullable = false)
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    private Date messageDate;

    //Hibernate
    Message(){

    }

    public Message(long id, User dest_user, User orig_user, String message, Date messageDate) {
        //this(id,dest_user.getId(),orig_user.getId(),message,messageDate);
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
