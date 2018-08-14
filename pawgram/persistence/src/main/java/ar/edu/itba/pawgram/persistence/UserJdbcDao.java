package ar.edu.itba.pawgram.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ar.edu.itba.pawgram.interfaces.UserDao;
import ar.edu.itba.pawgram.model.User;

@Repository
public class UserJdbcDao implements UserDao {
	private JdbcTemplate jdbcTemplate;
	private final static RowMapper<User> ROW_MAPPER = new RowMapper<User>() {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new User(rs.getInt("id"), rs.getString("name"), rs.getString("surname"), rs.getString("mail"), rs.getString("password"));
		}
	};
	
	@Autowired
	public UserJdbcDao(final DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
	}

	@Override
	public User findById(long id) {
	final List<User> list = jdbcTemplate.query("SELECT * FROM users WHERE userid = ?", ROW_MAPPER, id);
	if (list.isEmpty()) {
		return null;
	}
	return list.get(0);
	}

}
