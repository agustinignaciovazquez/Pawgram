package ar.edu.itba.pawgram.service;

import ar.edu.itba.pawgram.interfaces.service.PostService;
import ar.edu.itba.pawgram.interfaces.persistence.SearchZoneDao;
import ar.edu.itba.pawgram.interfaces.service.SearchZoneService;
import ar.edu.itba.pawgram.model.*;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;
import ar.edu.itba.pawgram.model.interfaces.PlainSearchZone;
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
    public SearchZone createSearchZone(Location location, int range, long userId) {
        return searchZoneDao.createSearchZone(location, range, userId);
    }

    @Override
    public boolean deleteZoneById(long zoneId) {
        return searchZoneDao.deleteZoneById(zoneId);
    }

    @Override
    public List<PlainSearchZone> getPlainSearchZonesByUser(User user) {
        return searchZoneDao.getPlainSearchZonesByUser(user.getId());
    }

    @Override
    public SearchZone getFullSearchZonesByIdWithoutPosts(long id) {
        return searchZoneDao.getFullSearchZoneById(id).build();
    }

    @Override
    public long getTotalSearchZonesByUser(User user) {
        return searchZoneDao.getTotalSearchZonesByUser(user);
    }

    @Override
    @Transactional
    public SearchZone getFullSearchZoneById(long zoneId, long page, int pageSize) {
        SearchZone.SearchZoneBuilder builder = searchZoneDao.getFullSearchZoneById(zoneId);
        if(builder == null)
            return null;
        List<PlainPost> posts = postService.getPlainPostsPaged(builder.getLocation(),builder.getRange(),page,pageSize);
        long max_page = postService.getMaxPage(pageSize,builder.getLocation(),builder.getRange());
        return builder.posts(posts).max_page(max_page).build();
    }

    @Override
    @Transactional
    public SearchZone getFullSearchZoneByIdAndCategory(long zoneId, Category category, long page, int pageSize) {
        SearchZone.SearchZoneBuilder builder = searchZoneDao.getFullSearchZoneById(zoneId);
        if(builder == null)
            return null;
        List<PlainPost> posts = postService.getPlainPostsByCategoryPaged(builder.getLocation(),builder.getRange(),
                category,page,pageSize);
        long max_page = postService.getMaxPageByCategory(pageSize,builder.getLocation(),builder.getRange(),category);
        return builder.posts(posts).max_page(max_page).build();
    }

    @Override
    @Transactional
    public List<SearchZone> getFullSearchZonesById(User user, long page, int pageSize) {
        List<PlainSearchZone> searchZonesPlain = searchZoneDao.getPlainSearchZonesByUser(user.getId());
        List<SearchZone> searchZones = new ArrayList<>();
        for(PlainSearchZone searchZone: searchZonesPlain){
            searchZones.add(getFullSearchZoneById(searchZone.getId(),page,pageSize));
        }
        return searchZones;
    }

    @Override
    @Transactional
    public List<SearchZone> getFullSearchZonesByIdAndCategory(User user, Category category, long page, int pageSize) {
        List<PlainSearchZone> searchZonesPlain = searchZoneDao.getPlainSearchZonesByUser(user.getId());
        List<SearchZone> searchZones = new ArrayList<>();
        for(PlainSearchZone searchZone: searchZonesPlain){
            searchZones.add(getFullSearchZoneByIdAndCategory(searchZone.getId(),category,page,pageSize));
        }
        return searchZones;
    }


}
