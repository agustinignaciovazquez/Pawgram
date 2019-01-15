package ar.edu.itba.pawgram.interfaces.persistence;

import ar.edu.itba.pawgram.model.Message;
import ar.edu.itba.pawgram.model.User;

import java.util.Date;
import java.util.List;

public interface MessageDao {
    /**
     * Lists {@link Message} of two specific {@link User} origin and destination sorted by date.
     * @param origin - origin user.
     * @param destination - destination user
     * @param limit - result limit
     * @param offset - offset
     * @return {@link List} of messages associated with the two {@link User}.
     * 		   Could be empty if there are no messages between two users
     */
    public List<Message> getMessages(User origin, User destination, int limit, int offset);

    /**
     * Creates a new {@link Message} between two specific {@link User} origin and destination
     * @param origin - origin user.
     * @param destination - destination user
     * @return {@link Message} created
     */
    public Message sendMessage(final User origin, final User destination,final String content, final Date date);

    /**
     * Lists {@link User} that {@link User} have chat with
     * @param origin - origin user.
     * @return {@link List} of {@link User}.
     * 		   Could be empty if the origin user have not chat with anyone
     */
    public List<User> getMessageUsers(final User origin);

    /**
     * Retrieves the total amount of messages from a conversation between two users
     * @param origin - origin user
     * @param destination - destination user
     * @return The number of messages.
     */
    public long getTotalMessages(final User origin, final User destination);
}
