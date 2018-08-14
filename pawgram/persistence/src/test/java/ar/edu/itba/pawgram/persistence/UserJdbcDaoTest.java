package ar.edu.itba.pawgram.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import ar.edu.itba.pawgram.model.User;

@Sql("classpath:schema.sql") 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserJdbcDaoTest {
	private static final String NAME = "Agustin";
	private static final String SURNAME = "Vazquez";
	private static final String MAIL = "agvazquez@itba.edu.ar";
	private static final String PASSWORD = "password";
	
	@Autowired
	private DataSource ds;
	@Autowired
	private UserJdbcDao userDao;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private JdbcTemplate jdbcTemplate;
	@Before
	public void setUp() {
		jdbcTemplate = new JdbcTemplate(ds);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
	}
	
	@Test
	public void testCreate() {
		final User user = userDao.create(NAME,SURNAME,MAIL,PASSWORD);
		assertNotNull(user);
		assertEquals(NAME, user.getName());
		assertTrue(bCryptPasswordEncoder.matches(PASSWORD, user.getPassword()));
		assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
	}

}
