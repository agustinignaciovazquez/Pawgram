package ar.edu.itba.pawgram.service;

import ar.edu.itba.pawgram.interfaces.service.PostService;
import ar.edu.itba.pawgram.interfaces.persistence.SearchZoneDao;
import ar.edu.itba.pawgram.interfaces.service.SearchZoneService;
import ar.edu.itba.pawgram.model.*;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;
import ar.edu.itba.pawgram.model.interfaces.PlainSearchZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public boolean deleteZoneById(long zoneId, User user) {
        return searchZoneDao.deleteZoneById(zoneId, user);
    }

    @Override
    public List<PlainSearchZone> getPlainSearchZonesByUser(User user) {
        return searchZoneDao.getPlainSearchZonesByUser(user.getId());
    }

    @Override
    public List<SearchZone> getSearchZonesByUser(User user) {
        List<SearchZone.SearchZoneBuilder> builders = searchZoneDao.getSearchZonesByUserId(user.getId());
        List<SearchZone> searchZones = new ArrayList<>();
        for(SearchZone.SearchZoneBuilder builder: builders){
            List<PlainPost> posts = postService.getPlainPosts(builder.getLocation(),builder.getRange());
            searchZones.add(builder.user(user).posts(posts).build());
        }
        return searchZones;
    }

    @Override
    public List<SearchZone> getSearchZonesByUserPaged(User user, int page, int pageSize) {
        List<SearchZone.SearchZoneBuilder> builders = searchZoneDao.getSearchZonesByUserId(user.getId());
        List<SearchZone> searchZones = new ArrayList<>();
        for(SearchZone.SearchZoneBuilder builder: builders){
            List<PlainPost> posts = postService.getPlainPostsPaged(builder.getLocation(),builder.getRange(),page,pageSize);
            searchZones.add(builder.user(user).posts(posts).build());
        }
        return searchZones;
    }

    @Override
    public List<SearchZone> getSearchZonesByUserAndCategory(User user, Category category) {
        List<SearchZone.SearchZoneBuilder> builders = searchZoneDao.getSearchZonesByUserId(user.getId());
        List<SearchZone> searchZones = new ArrayList<>();
        for(SearchZone.SearchZoneBuilder builder: builders){
            List<PlainPost> posts = postService.getPlainPostsByCategory(builder.getLocation(),builder.getRange(),category);
            searchZones.add(builder.user(user).posts(posts).build());
        }
        return searchZones;
    }

    @Override
    public List<SearchZone> getSearchZonesByUserAndCategoryPaged(User user, Category category,int page, int pageSize) {
        List<SearchZone.SearchZoneBuilder> builders = searchZoneDao.getSearchZonesByUserId(user.getId());
        List<SearchZone> searchZones = new ArrayList<>();
        for(SearchZone.SearchZoneBuilder builder: builders){
            List<PlainPost> posts = postService.getPlainPostsByCategoryPaged(builder.getLocation(),builder.getRange(),
                    category,page,pageSize);
            searchZones.add(builder.user(user).posts(posts).build());
        }
        return searchZones;
    }

}
