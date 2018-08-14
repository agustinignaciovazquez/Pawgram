package ar.edu.itba.pawgram.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import ar.edu.itba.pawgram.interfaces.UserDao;
import ar.edu.itba.pawgram.model.User;

@Repository
public class UserJdbcDao implements UserDao {
	private JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert jdbcInsert;
	private final static RowMapper<User> ROW_MAPPER = new RowMapper<User>() {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new User(rs.getInt("id"), rs.getString("name"), rs.getString("surname"), rs.getString("mail"), rs.getString("password"));
		}
	};
	
	@Autowired
	public UserJdbcDao(final DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("users")
				.usingGeneratedKeyColumns("id");

	}

	@Override
	public User findById(long id) {
	final List<User> list = jdbcTemplate.query("SELECT * FROM users WHERE id = ?", ROW_MAPPER, id);
	if (list.isEmpty()) {
		return null;
	}
	return list.get(0);
	}

	@Override
	public User create(String name, String surname, String mail, String password) {
		final Map<String, Object> args = new HashMap<>();
		args.put("name", name); 
		args.put("surname", surname); 
		args.put("mail", mail); 
		args.put("password", password); 
		final Number userId = jdbcInsert.executeAndReturnKey(args);
		return new User(userId.longValue(),name,surname,mail,password);
	}

	@Override
	public User findByMail(String mail) {
		final List<User> list = jdbcTemplate.query("SELECT * FROM users WHERE mail = ?", ROW_MAPPER, mail);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

}
