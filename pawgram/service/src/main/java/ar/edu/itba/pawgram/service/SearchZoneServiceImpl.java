package ar.edu.itba.pawgram.service;

import ar.edu.itba.pawgram.interfaces.exception.InvalidSearchZoneException;
import ar.edu.itba.pawgram.interfaces.exception.MaxSearchZoneReachedException;
import ar.edu.itba.pawgram.interfaces.service.PostService;
import ar.edu.itba.pawgram.interfaces.persistence.SearchZoneDao;
import ar.edu.itba.pawgram.interfaces.service.SearchZoneService;
import ar.edu.itba.pawgram.model.*;
import ar.edu.itba.pawgram.model.structures.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchZoneServiceImpl implements SearchZoneService {
    @Autowired
    private SearchZoneDao searchZoneDao;
    @Autowired
    private PostService postService;

    @Override
    @Transactional
    public SearchZone createSearchZone(Location location, int range, User user) throws MaxSearchZoneReachedException, InvalidSearchZoneException {
        final long userTotalSearchZones = getTotalSearchZonesByUser(user);
        if(userTotalSearchZones >= MAX_SEARCH_ZONES)
            throw new MaxSearchZoneReachedException();

        if(!(range <= MAX_RANGE_KM && range >= MIN_RANGE_KM))
            throw new InvalidSearchZoneException();

        return searchZoneDao.createSearchZone(location, range*1000, user);
    }

    @Override
    @Transactional
    public boolean deleteZoneById(long zoneId) {
        return searchZoneDao.deleteZoneById(zoneId);
    }

    @Override
    public List<SearchZone> getPlainSearchZonesByUser(User user) {
        return searchZoneDao.getPlainSearchZonesByUser(user.getId());
    }

    @Override
    public SearchZone getFullSearchZonesByIdWithoutPosts(long id) {
        return searchZoneDao.getFullSearchZoneById(id);
    }

    @Override
    public long getTotalSearchZonesByUser(User user) {
        return searchZoneDao.getTotalSearchZonesByUser(user);
    }

    @Override
    @Transactional
    public SearchZone getFullSearchZoneById(long zoneId, int page, int pageSize) {
        SearchZone sz = getFullSearchZonesByIdWithoutPosts(zoneId);
        if(sz == null)
            return null;

        SearchZone.SearchZoneBuilder builder = SearchZone.getBuilderFromSearchZone(sz);

        List<Post> posts = postService.getPlainPostsPaged(builder.getLocation(),builder.getRange(),page,pageSize);
        long max_page = postService.getMaxPage(pageSize,builder.getLocation(),builder.getRange());
        return builder.posts(posts).max_page(max_page).build();
    }

    @Override
    @Transactional
    public SearchZone getFullSearchZoneByIdAndCategory(long zoneId, Category category, int page, int pageSize) {
        SearchZone sz = getFullSearchZonesByIdWithoutPosts(zoneId);
        if(sz == null)
            return null;

        SearchZone.SearchZoneBuilder builder = SearchZone.getBuilderFromSearchZone(sz);

        List<Post> posts = postService.getPlainPostsByCategoryPaged(builder.getLocation(),builder.getRange(),
                category,page,pageSize);
        long max_page = postService.getMaxPageByCategory(pageSize,builder.getLocation(),builder.getRange(),category);
        return builder.posts(posts).max_page(max_page).build();
    }

    @Override
    @Transactional
    public List<SearchZone> getFullSearchZonesById(User user, int page, int pageSize) {
        List<SearchZone> searchZonesPlain = searchZoneDao.getPlainSearchZonesByUser(user.getId());
        List<SearchZone> searchZones = new ArrayList<>();
        for(SearchZone searchZone: searchZonesPlain){
            searchZones.add(getFullSearchZoneById(searchZone.getId(),page,pageSize));
        }
        return searchZones;
    }

    @Override
    @Transactional
    public List<SearchZone> getFullSearchZonesByIdAndCategory(User user, Category category, int page, int pageSize) {
        List<SearchZone> searchZonesPlain = searchZoneDao.getPlainSearchZonesByUser(user.getId());
        List<SearchZone> searchZones = new ArrayList<>();
        for(SearchZone searchZone: searchZonesPlain){
            searchZones.add(getFullSearchZoneByIdAndCategory(searchZone.getId(),category,page,pageSize));
        }
        return searchZones;
    }


}
