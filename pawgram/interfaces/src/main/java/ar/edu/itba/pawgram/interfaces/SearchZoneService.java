package ar.edu.itba.pawgram.interfaces;

import ar.edu.itba.pawgram.model.*;
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
     * @param zoneId - ID of the post to delete
     * @param user - user calling the method to check if has privileges
     * @return true if a product was deleted
     */
    public boolean deleteZoneById(final long zoneId, User user);

    /**
     * Lists searchZones of a specific {@link User} sorted by minor distance first,
     * @param user - the user searching in his zones.
     * @return {@link List} of searchZone associated with the {@link User}
     */
    public List<PlainSearchZone> getPlainSearchZonesByUser(final User user);

    /**
     * Lists searchZones of a specific {@link User} sorted by minor distance first,
     * @param user - the user searching in his zones.
     * @return {@link List} of searchZone associated with the {@link User}
     * and in each {@link SearchZone} the list of {@link Post} that matches the criteria.
     */
    public List<SearchZone> getSearchZonesByUser(final User user);

    /**
     * Lists searchZones of a specific {@link User} sorted by minor distance first,
     * @param user - the user searching in his zones.
     * @param page - number of page
     * @param pageSize - max number of results per page
     * @return {@link List} of searchZone associated with the {@link User}
     * and in each {@link SearchZone} the list of {@link Post} that matches the criteria.
     */
    public List<SearchZone> getSearchZonesByUserPaged(final User user, final int page, final int pageSize);
    /**
     * Lists searchZones of a specific {@link User} sorted by minor distance first,
     * @param user - the user searching in his zones.
     * @return {@link List} of searchZone associated with the {@link User}
     * and in each {@link SearchZone} the list of {@link Post} that matches the criteria.
     */
    public List<SearchZone> getSearchZonesByUserAndCategory(final User user, final Category category);
    /**
     * Lists searchZones of a specific {@link User} sorted by minor distance first,
     * @param user - the user searching in his zones.
     * @param page - number of page
     * @param pageSize - max number of results per page
     * @return {@link List} of searchZone associated with the {@link User}
     * and in each {@link SearchZone} the list of {@link Post} that matches the criteria.
     */
    public List<SearchZone> getSearchZonesByUserAndCategoryPaged(final User user, final Category category,
                                                                 final int page, final int pageSize);

}
