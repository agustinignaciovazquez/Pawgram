package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.persistence.SearchZoneDao;
import ar.edu.itba.pawgram.model.SearchZone;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.model.structures.Location;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class SearchZoneHibernateDao implements SearchZoneDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public SearchZone createSearchZone(Location location, int range, User user) {
        final SearchZone.SearchZoneBuilder szb = SearchZone.getBuilder(location,range);

        final SearchZone sz = szb.user(user).build();
        em.persist(sz);

        return sz;
    }

    @Override
    public boolean deleteZoneById(long zoneId) {
        final SearchZone sz = getPlainSearchZonesById(zoneId);

        if (sz != null)
            em.remove(sz);

        return (sz != null);
    }

    @Override
    public SearchZone getPlainSearchZonesById(long id) {
        return em.find(SearchZone.class,id);
    }

    @Override
    public List<SearchZone> getPlainSearchZonesByUser(long userId) {
        final TypedQuery<SearchZone> query = em.createQuery("from SearchZone as s where s.user.id = :userId", SearchZone.class);
        query.setParameter("userId", userId);
        final List<SearchZone> l = query.getResultList();

        return l;
    }

    @Override
    public SearchZone getFullSearchZoneById(long zoneId) {
        final TypedQuery<SearchZone> query = em.createQuery("from SearchZone as s join fetch s.user as su where s.id = :zoneId", SearchZone.class);
        query.setParameter("zoneId", zoneId);
        final List<SearchZone> result = query.getResultList();
        if (result.isEmpty())
            return null;

        return result.get(0);
    }

    @Override
    public long getTotalSearchZonesByUser(long userId) {
        final TypedQuery<Long> query = em.createQuery("select count(*) from SearchZone as s where s.user.id= :userId", Long.class);
        query.setParameter("userId",userId);
        final Long total = query.getSingleResult();

        return total != null ? total : 0;
    }
}
