package ar.edu.itba.pawgram.model;

import java.time.LocalDateTime;

public class Message {
    private final long id;
    private final long dest_user_id;
    private final long orig_user_id;
    private final String message;
    private final LocalDateTime messageDate;

    public Message(long id, long dest_user_id, long orig_user_id, String message, LocalDateTime messageDate) {
        this.id = id;
        this.dest_user_id = dest_user_id;
        this.orig_user_id = orig_user_id;
        this.message = message;
        this.messageDate = messageDate;
    }

    public long getId() {
        return id;
    }

    public long getDest_user_id() {
        return dest_user_id;
    }

    public long getOrig_user_id() {
        return orig_user_id;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getMessageDate() {
        return messageDate;
    }
}
