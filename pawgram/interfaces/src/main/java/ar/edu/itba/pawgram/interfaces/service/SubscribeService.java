package ar.edu.itba.pawgram.interfaces.service;

import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;

public interface SubscribeService {
    /**
     * Votes a {@link Post} given it's ID and the subscriber {@link User} ID.
     * @param postId - ID of the product to vote
     * @param userId - ID of the subscriber
     * @return true if the post was successfully voted and was not prevously
     *         subscribed, false otherwise
     */
    public boolean subscribePost(final long postId, final long userId);

    /**
     * Removes a vote from a {@link Post} given it's ID and the {@link User}
     * ID who had subscribed to the post.
     *   @param postId - ID of the product to vote
      * @param userId - ID of the subscriber
     * @return true if the post was successfully unsubscribed and was prevously
     *         subscribed, false otherwise
     */
    public boolean unsubscribePost(final long postId, final long userId);

    /**
     * True if  the {@link User} is subscribed to the {@link Post}, false otherwise
     * @param postId - ID of the product to vote
     * @param userId - ID of the subscriber
     * @return boolean corresponding. Also if any of ids is invalid returns false
     */
    public boolean isSubscribedToPost(final long postId, final long userId);
}
