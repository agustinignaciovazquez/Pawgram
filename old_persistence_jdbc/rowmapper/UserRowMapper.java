package ar.edu.itba.pawgram.persistence.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ar.edu.itba.pawgram.model.User;

@Component
public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new User(rs.getInt("id"), rs.getString("name"),
				rs.getString("surname"), rs.getString("mail"),
				rs.getString("password"), rs.getString("profile_img_url"));
	}

}
