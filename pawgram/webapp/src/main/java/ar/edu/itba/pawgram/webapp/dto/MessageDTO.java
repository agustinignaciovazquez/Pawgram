package ar.edu.itba.pawgram.webapp.dto;

import ar.edu.itba.pawgram.model.Message;
import ar.edu.itba.pawgram.model.User;

import javax.xml.bind.annotation.XmlElement;
import java.net.URI;
import java.util.Date;
import java.util.Optional;

public class MessageDTO {
    private long id;
    private long dest_user;
    private long orig_user;

    @XmlElement(name = "message_date")
    private Date message_date;

    public MessageDTO() {
    }

    public MessageDTO(final Message message, final URI baseUri){
        this.id = message.getId();
        this.dest_user = message.getDest_user().getId();
        this.orig_user = message.getOrig_user().getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDest_user() {
        return dest_user;
    }

    public void setDest_user(long dest_user) {
        this.dest_user = dest_user;
    }

    public long getOrig_user() {
        return orig_user;
    }

    public void setOrig_user(long orig_user) {
        this.orig_user = orig_user;
    }

    public Date getMessage_date() {
        return message_date;
    }

    public void setMessage_date(Date message_date) {
        this.message_date = message_date;
    }
}
