package ar.edu.itba.pawgram.interfaces.service;

import ar.edu.itba.pawgram.model.*;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;
import ar.edu.itba.pawgram.model.interfaces.PlainSearchZone;

import java.util.List;

public interface SearchZoneService {
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
     * @param zoneId - ID of the zone to delete
     * @return true if a zone was deleted
     */
    public boolean deleteZoneById(final long zoneId);

    /**
     * Lists searchZones of a specific {@link User} sorted by minor distance first,
     * @param user - the user searching in his zones.
     * @return {@link List} of searchZone associated with the {@link User}
     */
    public List<PlainSearchZone> getPlainSearchZonesByUser(final User user);

    /**
     * Find a {@link SearchZone} by the id,
     * @param zoneId - ID of the zone
     * @param page - current page for {@link List} of {@link PlainPost}
     * @param page - max size of page for {@link List} of {@link PlainPost}
     * @return searchZone associated with the {@link User}(owner)
     * and  the list of {@link PlainPost} paged with the params
     * or null if the zoneId is invalid
     */
    public SearchZone getFullSearchZoneById(final long zoneId, final long page, final int pageSize);

    /**
     * Find a {@link SearchZone} by the id,
     * @param zoneId - ID of the zone
     * @param page - current page for {@link List} of {@link PlainPost}
     * @param category - filter the {@link List} of {@link PlainPost} by this
     * @param page - max size of page for {@link List} of {@link PlainPost}
     * @return searchZone associated with the {@link User}(owner)
     * and  the list of {@link PlainPost} paged with the params
     * or null if the zoneId is invalid
     */
    public SearchZone getFullSearchZoneByIdAndCategory(final long zoneId, final Category category, final long page, final int pageSize);



}
