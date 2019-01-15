package ar.edu.itba.pawgram.interfaces.service;

import ar.edu.itba.pawgram.model.Message;
import ar.edu.itba.pawgram.model.User;

import java.util.List;

public interface MessageService {
    /**
     * Creates {@link List} of {@link Message}between two specific {@link User} origin and destination sorted by date.
     * @param origin - origin user.
     * @param destination - destination user
     * @param page - number of page
     * @param pageSize - max number of results per page
     * @return {@link List} of messages
     * 	    Could be empty if there are no messages between two users
     */
    public List<Message> getMessages(final User origin, final User destination, final int page, final int pageSize);

    /**
     * Creates a new {@link Message} between two specific {@link User} origin and destination
     * @param origin - origin user.
     * @param destination - destination user
     * @return {@link Message} created
     */
    public Message sendMessage(final User origin, final User destination,final String content);

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

    /**
     * Retrieves the max page of messages from a conversation between two users
     * @param origin - origin user
     * @param destination - destination user
     * @param pageSize - the size of the page
     * @return The max page for messages.
     */
    public long getMaxPage(final User origin, final User destination,final int pageSize);
}
