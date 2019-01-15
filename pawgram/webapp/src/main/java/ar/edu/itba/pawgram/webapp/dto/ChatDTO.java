package ar.edu.itba.pawgram.webapp.dto;

import ar.edu.itba.pawgram.model.Message;
import ar.edu.itba.pawgram.model.User;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ChatDTO {
    private List<MessageDTO> messages;
    private UserDTO loggedUser;
    private UserDTO otherUser;
    private long totalCount;
    private long count;

    public ChatDTO() {
    }

    public ChatDTO(final List<Message> messages, long totalCount, final URI baseUri, final User loggedUser, final User otherUser) {
        this.messages = new LinkedList<>();
        this.loggedUser = new UserDTO(loggedUser,baseUri);
        this.otherUser = new UserDTO(otherUser,baseUri);

        for (final Message p : messages)
            this.messages.add(new MessageDTO(p, baseUri));

        this.totalCount = totalCount;
        this.count = messages.size();
    }

    public UserDTO getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(UserDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    public UserDTO getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(UserDTO otherUser) {
        this.otherUser = otherUser;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
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
