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

    @Override
    @Transactional
    public SearchZone createSearchZone(Location location, int range, User user) throws MaxSearchZoneReachedException, InvalidSearchZoneException {
        final long userTotalSearchZones = getTotalSearchZonesByUser(user.getId());
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
    public List<SearchZone> getPlainSearchZonesByUser(long userId) {
        return searchZoneDao.getPlainSearchZonesByUser(userId);
    }

    @Override
    public SearchZone getFullSearchZoneById(long id) {
        return searchZoneDao.getFullSearchZoneById(id);
    }

    @Override
    public long getTotalSearchZonesByUser(long userId) {
        return searchZoneDao.getTotalSearchZonesByUser(userId);
    }

}
