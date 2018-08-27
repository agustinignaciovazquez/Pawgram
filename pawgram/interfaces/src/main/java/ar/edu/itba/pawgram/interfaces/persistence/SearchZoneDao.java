package ar.edu.itba.pawgram.interfaces.persistence;

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
     * @return true if a product was deleted
     */
    public boolean deleteZoneById(final long zoneId);

    /**
     * Lists {@link PlainSearchZone} of a specific {@link User} sorted by minor distance first,
     * @param userId - ID of the user.
     * @return {@link List} of searchZone associated with the {@link User}
     */
    public List<PlainSearchZone> getPlainSearchZonesByUser(final long userId);

    /**
     * Gets a  {@link SearchZone} builder of a specific id,
     * @param zoneId - ID of the zone.
     * @return {@link SearchZone}  associated with the id.
     * 		   null if the id does not exists
     */
    public SearchZone.SearchZoneBuilder getFullSearchZoneById(final long zoneId);
}
