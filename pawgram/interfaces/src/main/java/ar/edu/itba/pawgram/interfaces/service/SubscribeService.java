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
     * @return true if the product was successfully unsuccessfully and was prevously
     *         subscribed, false otherwise
     */
    public boolean unsubscribePost(final long postId, final long userId);
}
