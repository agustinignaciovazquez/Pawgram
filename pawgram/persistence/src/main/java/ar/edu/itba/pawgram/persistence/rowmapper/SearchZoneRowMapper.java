package ar.edu.itba.pawgram.persistence.rowmapper;

import ar.edu.itba.pawgram.model.Location;
import ar.edu.itba.pawgram.model.SearchZone;
import ar.edu.itba.pawgram.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SearchZoneRowMapper implements RowMapper<SearchZone.SearchZoneBuilder> {
    @Autowired
    private UserRowMapper userRowMapper;

    @Override
    public SearchZone.SearchZoneBuilder mapRow(ResultSet rs, int rowNum) throws SQLException {
        return SearchZone.getBuilder(rs.getLong("zoneId"), new Location(rs.getDouble("longitude"),
                rs.getDouble("latitude")),rs.getInt("range")).user(userRowMapper.mapRow(rs, rowNum));
    }
}
