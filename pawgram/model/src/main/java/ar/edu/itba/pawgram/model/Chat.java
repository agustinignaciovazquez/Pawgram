package ar.edu.itba.pawgram.model;

import java.util.List;

public class Chat {
    private final User current;
    private final User other;
    private final List<Message> messages;

    public Chat(User current, User other, List<Message> messages) {
        this.current = current;
        this.other = other;
        this.messages = messages;
    }

    public User getCurrent() {
        return current;
    }

    public User getOther() {
        return other;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
