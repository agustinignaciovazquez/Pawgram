package ar.edu.itba.pawgram.interfaces.service;

import ar.edu.itba.pawgram.model.Chat;
import ar.edu.itba.pawgram.model.Message;
import ar.edu.itba.pawgram.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageService {
    /**
     * Creates {@link Chat} between two specific {@link User} origin and destination sorted by date.
     * @param origin - origin user.
     * @param destination - destination user
     * @return {@link List} of messages associated with the two {@link User}.
     * 		   Could be empty if there are no messages between two users
     */
    public Chat getMessages(final User origin, final User destination);

    /**
     * Creates a new {@link Message} between two specific {@link User} origin and destination
     * @param origin - origin user.
     * @param destination - destination user
     * @return {@link Message} created
     */
    public Message sendMessage(final User origin, final User destination,final String content);
}
