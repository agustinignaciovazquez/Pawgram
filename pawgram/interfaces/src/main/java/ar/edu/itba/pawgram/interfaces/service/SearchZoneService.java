package ar.edu.itba.pawgram.interfaces.service;

import ar.edu.itba.pawgram.interfaces.exception.InvalidSearchZoneException;
import ar.edu.itba.pawgram.interfaces.exception.MaxSearchZoneReachedException;
import ar.edu.itba.pawgram.model.*;
import ar.edu.itba.pawgram.model.structures.Location;

import java.util.List;

public interface SearchZoneService {
    public static final int MAX_SEARCH_ZONES = 3;
    public static final int MAX_RANGE_KM = 15;
    public static final int MIN_RANGE_KM = 1;
    /**
     * Creates a {@link SearchZone} inserting it into the database.
     * @param location - {@link Location} latitude and longitude center of circle
     * @param range - range circle
     * @param user - {@link User} that registers to a zone
     * @return The created {@link SearchZone}
     */
    public SearchZone createSearchZone(final Location location, final int range, final User user) throws MaxSearchZoneReachedException, InvalidSearchZoneException;

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
    public List<SearchZone> getPlainSearchZonesByUser(final User user);

    /**
     * Lists {@link SearchZone} with a specific id,
     * no posts, only user location and range
     * @param id - the search zone id.
     * @return searchZone associated with the id
     */
    public SearchZone getFullSearchZonesByIdWithoutPosts(final long id);

    /**
     * Retrieves the quantity of {@link SearchZone} registered for a given {@link User}
     * @param user - the user searching in his zones.
     * @return The number of {@link SearchZone}.
     */
    public long getTotalSearchZonesByUser(final User user);

    /**
     * Find a {@link SearchZone} by the id,
     * @param zoneId - ID of the zone
     * @param page - current page for {@link List} of {@link Post}
     * @param page - max size of page for {@link List} of {@link Post}
     * @return searchZone associated with the {@link User}(owner)
     * and  the list of {@link Post} paged with the params
     * or null if the zoneId is invalid
     */
    public SearchZone getFullSearchZoneById(final long zoneId, final int page, final int pageSize);

    /**
     * Find a {@link SearchZone} by the id,
     * @param zoneId - ID of the zone
     * @param page - current page for {@link List} of {@link Post}
     * @param category - filter the {@link List} of {@link Post} by this
     * @param page - max size of page for {@link List} of {@link Post}
     * @return searchZone associated with the {@link User}(owner)
     * and  the list of {@link Post} paged with the params
     * or null if the zoneId is invalid
     */
    public SearchZone getFullSearchZoneByIdAndCategory(final long zoneId, final Category category, final int page, final int pageSize);

    /**
     * Find all the {@link SearchZone} by the user,
     * @param user - the user in question
     * @param page - current page for {@link List} of {@link Post}
     * @param page - max size of page for {@link List} of {@link Post}
     * @return searchZone associated with the {@link User}(owner)
     * and  the list of {@link Post} paged with the params
     * or null if the zoneId is invalid
     */
    public List<SearchZone> getFullSearchZonesById(final User user, final int page, final int pageSize);

    /**
     * Find all the {@link SearchZone} by the user and category,
     * @param user - the user in question
     * @param page - current page for {@link List} of {@link Post}
     * @param category - filter the {@link List} of {@link Post} by this
     * @param page - max size of page for {@link List} of {@link Post}
     * @return searchZone associated with the {@link User}(owner)
     * and  the list of {@link Post} paged with the params
     * or null if the zoneId is invalid
     */
    public List<SearchZone> getFullSearchZonesByIdAndCategory(final User user, final Category category, final int page, final int pageSize);



}
