package ar.edu.itba.pawgram.interfaces;

import ar.edu.itba.pawgram.model.Location;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.SearchZone;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.model.interfaces.PlainSearchZone;

import java.util.List;

public interface SearchZoneDao {
    /**
     * Creates a {@link SearchZone} inserting it into the database.
     * @param location - {@link Location}
     * @param range -
     * @param userId - ID of the {@link User} that registers to a zone
     * @return The created {@link SearchZone}
     */
    public SearchZone createSearchZone(final Location location, final int range, final long userId);

    /**
     * Deletes a {@link SearchZone} from the database.
     * @param zoneId - ID of the post to delete
     * @param user - user calling the method to check if has privileges
     * @return true if a product was deleted
     */
    public boolean deleteZoneById(final long zoneId, User user);

    /**
     * Lists {@link PlainSearchZone} of a specific {@link User} sorted by minor distance first,
     * @param userId - ID of the user.
     * @return {@link List} of searchZone associated with the {@link User}
     */
    public List<PlainSearchZone> getPlainSearchZonesByUser(final long userId);

    /**
     * Lists {@link SearchZone} builder of a specific {@link User} sorted by minor distance first,
     * @param userId - ID of the user.
     * @return {@link List} of searchZone associated with the {@link User}.
     * 		   Could be empty if the user hasn't set a searchZone.
     */
    public List<SearchZone.SearchZoneBuilder> getSearchZonesByUserId(final long userId);
}
