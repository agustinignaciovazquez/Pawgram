package ar.edu.itba.pawgram.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import ar.edu.itba.pawgram.interfaces.persistence.UserDao;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.persistence.rowmapper.UserRowMapper;

@Repository
public class UserJdbcDao implements UserDao {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert jdbcInsert;
	@Autowired
	private UserRowMapper ROW_MAPPER;
	
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
	public User create(String name, String surname, String mail, String password, String profile_url) throws DuplicateEmailException {
		final Map<String, Object> args = new HashMap<>();
		String enc_password = bCryptPasswordEncoder.encode(password);
		args.put("name", name); 
		args.put("surname", surname); 
		args.put("mail", mail); 
		args.put("password", enc_password);
		args.put("profile_img_url", profile_url);

		try {
			final Number userId = jdbcInsert.executeAndReturnKey(args);
			return new User(userId.longValue(),name,surname,mail,enc_password,profile_url);
		}
		catch (DuplicateKeyException e) {
			throw new DuplicateEmailException("There already exists an user with email: " + mail);
		}
	}

	@Override
	public User changePassword(final long id,final String password) {
		User user = findById(id);
		String enc_password = bCryptPasswordEncoder.encode(password);
		if (user != null)
			jdbcTemplate.update("UPDATE users SET password = ? WHERE id = ?", enc_password, id);

		return new User(id,user.getName(),user.getSurname(),user.getMail(),enc_password,user.getProfile_img_url());
	}

	@Override
	public User changeName(long id, String name, String surname) {
		User user = findById(id);
		if (user != null)
			jdbcTemplate.update("UPDATE users SET name = ?, surname = ? WHERE id = ?", name,surname, id);

		return new User(id,name,surname,user.getMail(),user.getPassword(),user.getProfile_img_url());
	}

	@Override
	public User changeProfile(long id, String img_url) {
		User user = findById(id);
		if (user != null)
			jdbcTemplate.update("UPDATE users SET profile_img_url = ? WHERE id = ?", img_url, id);

		return new User(id,user.getName(),user.getSurname(),user.getMail(),user.getPassword(),img_url);
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
