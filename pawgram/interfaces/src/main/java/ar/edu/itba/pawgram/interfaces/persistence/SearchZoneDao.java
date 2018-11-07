package ar.edu.itba.pawgram.interfaces.persistence;

import ar.edu.itba.pawgram.model.structures.Location;
import ar.edu.itba.pawgram.model.SearchZone;
import ar.edu.itba.pawgram.model.User;

import java.util.List;

public interface SearchZoneDao {
    /**
     * Creates a {@link SearchZone} inserting it into the database.
     * @param location - {@link Location}
     * @param range -
     * @param user - {@link User} that registers to a zone
     * @return The created {@link SearchZone}
     */
    public SearchZone createSearchZone(Location location, int range, User user);

    /**
     * Deletes a {@link SearchZone} from the database.
     * @param zoneId - ID of the post to delete
     * @return true if a zone was deleted
     */
    public boolean deleteZoneById(final long zoneId);

    /**
     * {@link SearchZone} of a specific id,
     * @param id - ID of the search zone.
     * @return {@link SearchZone} if id exists, null otheriwise
     */
    public SearchZone getPlainSearchZonesById(final long id);

    /**
     * Lists {@link SearchZone} of a specific {@link User} sorted by minor distance first,
     * @param userId - ID of the user.
     * @return {@link List} of searchZone associated with the {@link User}
     */
    public List<SearchZone> getPlainSearchZonesByUser(final long userId);

    /**
     * Gets a  {@link SearchZone} builder of a specific id,
     * @param zoneId - ID of the zone.
     * @return {@link SearchZone}  associated with the id.
     * 		   null if the id does not exists
     */
    public SearchZone getFullSearchZoneById(final long zoneId);

    /**
     * Retrieves the quantity of {@link SearchZone} registered for a given {@link User}
     * @param user - the user searching in his zones.
     * @return The number of {@link SearchZone}.
     */
    public long getTotalSearchZonesByUser(final User user);
}
