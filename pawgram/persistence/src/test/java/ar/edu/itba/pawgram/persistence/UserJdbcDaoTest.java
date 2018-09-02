package ar.edu.itba.pawgram.persistence;

import static ar.edu.itba.pawgram.persistence.UserTestUtils.assertEqualsUsers;
import static ar.edu.itba.pawgram.persistence.UserTestUtils.dummyUser;
import static ar.edu.itba.pawgram.persistence.UserTestUtils.dummyUserList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.sql.DataSource;

import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
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

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class UserJdbcDaoTest {

	private final static int LIST_SIZE = 20;

	@Autowired
	private UserJdbcDao userDao;

	@Autowired
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() throws Exception {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
	}

	@Test
	public void createUserTest() throws DuplicateEmailException {
		User expected = dummyUser(0);
		User actual = userDao.create(expected.getName(), expected.getSurname(), expected.getMail(), expected.getPassword(), expected.getProfile_img_url());

		assertEqualsUsers(expected, actual);
		assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
	}

	@Test(expected = DuplicateEmailException.class)
	public void duplicateEmailExceptionTest() throws DuplicateEmailException {
		User dummyUser = dummyUser(0);
		insertUser(dummyUser);
		insertUser(dummyUser);
	}

	@Test
	public void getUserByEmailTest() throws DuplicateEmailException {
		User expected = dummyUser(0);
		insertUser(expected);

		User actual = userDao.findByMail(expected.getMail());

		assertEqualsUsers(expected, actual);
		assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
	}

	@Test
	public void getUserByIdTest() throws DuplicateEmailException {
		User expected = dummyUser(0);
		insertUser(expected);

		User actual = userDao.findById(expected.getId());

		assertEqualsUsers(expected, actual);
		assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
	}

	//TODO: Si implementamos keyword
	/*@Test
	public void getUserByKeywordTest() throws DuplicateEmailException {
		List<User> expected = dummyUserList(LIST_SIZE, 0);
		insertUsers(expected);
		String keyword = expected.get(0).getName().substring(0, 3);

		List<User> actual = userDao.getUsersByKeyword(keyword, LIST_SIZE);
		assertTrue(expected.containsAll(actual));
		assertTrue(actual.containsAll(expected));

		actual = userDao.getUsersByKeyword("vaz", LIST_SIZE);
		assertTrue(expected.containsAll(actual));
		assertTrue(actual.containsAll(expected));

		expected = actual.subList(0, 5);
		actual = userDao.getUsersByKeyword(keyword, 5);
		assertTrue(expected.containsAll(actual));
		assertTrue(actual.containsAll(expected));

		assertTrue(userDao.getUsersByKeyword("random", LIST_SIZE).isEmpty());

		assertEqualsUsers(dummyUser(0), userDao.getUsersByKeyword("0", LIST_SIZE).get(0));
	}*/

	private void insertUser(User user) throws DuplicateEmailException {
		userDao.create(user.getName(), user.getSurname(), user.getMail(), user.getPassword(), user.getProfile_img_url());
	}

	private void insertUsers(List<User> users) throws DuplicateEmailException {
		for (User user : users)
			insertUser(user);
	}

	//NOTE: tener en cuenta la pass que le ponemos al test
	public void changePasswordTest() throws DuplicateEmailException {
		User dummyUser = dummyUser(0);
		String expectedPassword = "test";
		User expected = new User(0, dummyUser.getName(), dummyUser.getSurname(), dummyUser.getMail(), expectedPassword, dummyUser.getProfile_img_url());
		userDao.create(dummyUser.getName(), dummyUser.getSurname(), dummyUser.getMail(), dummyUser.getPassword(), dummyUser.getProfile_img_url());

		User actual = userDao.changePassword(0, expectedPassword);

		assertEqualsUsers(expected, actual);
		assertEquals(expectedPassword, expected.getPassword());
	}

}