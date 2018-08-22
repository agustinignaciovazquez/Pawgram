package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.SearchZoneDao;
import ar.edu.itba.pawgram.model.Location;
import ar.edu.itba.pawgram.model.SearchZone;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.model.interfaces.PlainSearchZone;
import ar.edu.itba.pawgram.persistence.rowmapper.PlainSearchZoneRowMapper;
import ar.edu.itba.pawgram.persistence.rowmapper.SearchZoneRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SearchZoneJdbcDao implements SearchZoneDao {
    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    private SearchZoneRowMapper ROW_MAPPER;
    @Autowired
    private PlainSearchZoneRowMapper PLAIN_ROW_MAPPER;
    @Autowired
    public SearchZoneJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("search_zones")
                .usingGeneratedKeyColumns("zoneId");

    }
    @Override
    public SearchZone createSearchZone(Location location, int range, long userId) {
        final Map<String, Object> args = new HashMap<>();
        args.put("longitude", location.getLongitude());
        args.put("latitude", location.getLatitude());
        args.put("range", range);
        args.put("userId", userId);
        final Number zoneId = jdbcInsert.executeAndReturnKey(args);
        return SearchZone.getBuilder(zoneId.longValue(),location,range).build();
    }

    @Override
    public boolean deleteZoneById(long zoneId, User user) {
        Integer total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM search_zones, users WHERE zoneId = ? AND userId = ?",
                Integer.class,zoneId,user.getId());
        if(total == 0)
            return false;
        return jdbcTemplate.update("DELETE FROM search_zones WHERE zoneId = ?", zoneId) == 1;
    }

    @Override
    public List<PlainSearchZone> getPlainSearchZonesByUser(long userId) {
        return jdbcTemplate.query("SELECT zoneId, latitude, longitude, range" +
                        " FROM search_zones WHERE userId = ? ORDER BY id ASC",
                PLAIN_ROW_MAPPER,userId);
    }

    @Override
    public List<SearchZone.SearchZoneBuilder> getSearchZonesByUserId(long userId) {
        return jdbcTemplate.query("SELECT zoneId, latitude, longitude, range" +
                        " FROM search_zones WHERE userId = ? ORDER BY id ASC",
                ROW_MAPPER,userId);
    }
}
