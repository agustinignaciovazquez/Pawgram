import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
import ar.edu.itba.pawgram.interfaces.exception.InvalidUserException;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.persistence.UserHibernateDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
@Sql("classpath:schema.sql")
public class UserHibernateDaoTest {

    private final static int LIST_SIZE = 20;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserHibernateDao userDao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
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
        User actual = UserTestUtils.dummyUser(1);
        User expected = insertUser(actual);

        UserTestUtils.assertEqualsUsers(expected, actual);
        bCryptPasswordEncoder.matches(actual.getPassword(),expected.getPassword());
    }

    @Test(expected = DuplicateEmailException.class)
    public void createDuplicatedUserTest() throws DuplicateEmailException {
        User expected = UserTestUtils.dummyUser(1);
        expected = insertUser(expected);
        User actual = userDao.create(expected.getName(), expected.getSurname(),  expected.getMail(), expected.getPassword(),
                expected.getProfile_img_url());
    }


    @Test(expected = DuplicateEmailException.class)
    public void duplicateEmailExceptionTest() throws DuplicateEmailException {
        User dummyUser = UserTestUtils.dummyUser(1);
        insertUser(dummyUser);
        insertUser(dummyUser);
    }

    @Test
    public void getUserByEmailTest() throws DuplicateEmailException {
        User expected = UserTestUtils.dummyUser(1);
        expected = insertUser(expected);

        User actual = userDao.findByMail(expected.getMail());

        UserTestUtils.assertEqualsUsers(expected, actual);
        bCryptPasswordEncoder.matches(actual.getPassword(),expected.getPassword());
    }

    @Test
    public void getUserByIdTest() throws DuplicateEmailException {
        User expected = UserTestUtils.dummyUser(1);
        expected = insertUser(expected);

        User actual = userDao.findById(expected.getId());

        UserTestUtils.assertEqualsUsers(expected, actual);
    }

    private Set<String> stringToSet(final String str) {
        final String[] keywords = str.trim().split(" ");
        final Set<String> validKeywords = new HashSet<>();

        for (final String word : keywords)
            validKeywords.add(word);

        return validKeywords;
    }

    private void assertSortedByUsername(List<User> actual) {
        for (int i = 1; i < actual.size(); i++)
            assertTrue(actual.get(i).getName().compareTo(actual.get(i - 1).getName()) > 0);
    }

    @Test
    public void changePasswordTest() throws DuplicateEmailException, InvalidUserException {
        User dummyUser = UserTestUtils.dummyUser(1);
        String expectedPassword = "sucutrule";
        User expected = new User(dummyUser.getName(), dummyUser.getSurname(), dummyUser.getMail(), expectedPassword,
                dummyUser.getProfile_img_url());

        User inserted = insertUser(dummyUser);

        User actual = userDao.changePassword(inserted.getId(), expectedPassword);

        UserTestUtils.assertEqualsUsers(inserted, actual);
        assertEquals(expectedPassword, expected.getPassword());
    }

    @Test
    @Transactional
    public void getProfilePictureFromIdTest() throws DuplicateEmailException {
        User dummyUser = UserTestUtils.dummyUser(1);
        String expected = dummyUser.getProfile_img_url();
        dummyUser = userDao.create(dummyUser.getName(), dummyUser.getSurname(), dummyUser.getMail(), dummyUser.getPassword(), expected);
        expected = dummyUser.getProfile_img_url();

        String actual = userDao.getProfilePictureByUserId(dummyUser.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void changeProfilePictureTest() throws DuplicateEmailException, InvalidUserException {
        User dummyUser = UserTestUtils.dummyUser(1);
        String expected = "Modified image";

        User inserted = insertUser(dummyUser);

        userDao.changeProfile(inserted.getId(), expected);
        String actual = userDao.getProfilePictureByUserId(inserted.getId());

        assertEquals(expected, actual);
    }

    public User insertUser(User user) throws DuplicateEmailException {
        return userDao.create(user.getName(), user.getSurname(), user.getMail(), user.getPassword(), user.getProfile_img_url() );
    }

    private void insertUsers(List<User> users) throws DuplicateEmailException {
        for (User user : users)
            insertUser(user);
    }
}