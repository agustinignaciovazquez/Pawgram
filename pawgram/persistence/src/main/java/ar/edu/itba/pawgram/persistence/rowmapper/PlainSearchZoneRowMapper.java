package ar.edu.itba.pawgram.persistence.rowmapper;

import ar.edu.itba.pawgram.model.Location;
import ar.edu.itba.pawgram.model.SearchZone;
import ar.edu.itba.pawgram.model.interfaces.PlainSearchZone;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PlainSearchZoneRowMapper implements RowMapper<PlainSearchZone> {

    @Override
    public PlainSearchZone mapRow(ResultSet rs, int rowNum) throws SQLException {
        return SearchZone.getBuilder(rs.getLong("zoneId"), new Location(rs.getDouble("longitude"),
                rs.getDouble("latitude")),rs.getInt("range")).build();
    }
}
