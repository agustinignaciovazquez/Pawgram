package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
import ar.edu.itba.pawgram.interfaces.persistence.UserDao;
import ar.edu.itba.pawgram.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
@Sql("classpath:schema.sql")
public class PostHibernateDaoTest {

    private static final int CATEGORY_POST_EACH = 5;
    private static final int LIST_SIZE = Category.values().length * CATEGORY_POST_EACH;

    @Autowired
    private PostHibernateDao postDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    private List<User> dummyUserList;

    @Before
    public void setUp() throws Exception {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
        dummyUserList = UserTestUtils.dummyUserList(LIST_SIZE, 1);
        insertDummyUsers(dummyUserList);
    }

    private void assertEqualsList(List<Post> expected, List<Post> actual) {
        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++)
            PostTestUtils.assertEqualsPlainPosts(expected.get(i), actual.get(i));
    }

    @Test
    public void getPostByPostIdTest() throws DuplicateEmailException {
        Post expected = PostTestUtils.dummyPost(1);
        expected = insertPost(expected);

        Post actual = postDao.getFullPostById(1);

        PostTestUtils.assertEqualsFullPosts(expected, actual);
        assertNull(postDao.getFullPostById(2));

        PostTestUtils.assertEqualsPlainPosts(expected, postDao.getPlainPostById(1));
    }

    @Test
    public void getPlainPostsByUserIdTest() throws DuplicateEmailException {
        List<Post> expected = PostTestUtils.dummyPostListWithUserId(LIST_SIZE, 1, 1);
        expected = insertPosts(expected);

        List<Post> actual = postDao.getPlainPostsByUserIdRange(1, LIST_SIZE ,0);

        assertEqualsReversedSortedList(expected, actual);
    }


    @Test
    public void deletePostById() throws DuplicateEmailException {
        Post dummyPost = PostTestUtils.dummyPost(1);
        insertPost(dummyPost);

        assertTrue(postDao.deletePostById(1));
        assertFalse(postDao.deletePostById(1));
    }

    @Test
    public void getPlainPostsByKeywordMatchNameTest() throws DuplicateEmailException {
        List<Post> expected = PostTestUtils.dummyPostList(LIST_SIZE, 1);
        insertPosts(expected);
        String keyword = expected.get(0).getTitle().substring(0, 3);
        String noMatchKeyword = expected.get(0).getTitle().substring(1, 3);

        assertSearch(keyword, noMatchKeyword, expected);
    }

    @Test
    public void getTotalPostsTest() throws DuplicateEmailException {
        assertEquals(0, postDao.getTotalPosts());

        insertPost(PostTestUtils.dummyPost(1));
        assertEquals(1, postDao.getTotalPosts());

        postDao.deletePostById(1);
        assertEquals(0, postDao.getTotalPosts());
    }

    @Test
    public void getPlainPostsByKeywordMatchTaglineTest() throws DuplicateEmailException {
        List<Post> expected = PostTestUtils.dummyPostList(LIST_SIZE, 1);
        insertPosts(expected);
        String keyword = expected.get(0).getDescription().substring(0, 3);
        String noMatchKeyword = expected.get(0).getDescription().substring(1, 3);

        assertSearch(keyword, noMatchKeyword, expected);
        List<Post> actual = postDao.getPlainPostsByKeywordRange(stringToSet("desc"), LIST_SIZE, 0);

        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));
    }

    private Set<String> stringToSet(final String str) {
        final String[] keywords = str.trim().split(" ");
        final Set<String> validKeywords = new HashSet<>();

        for (final String word : keywords)
            validKeywords.add(word);

        return validKeywords;
    }

    private void assertSearch(String keyword, String noMatchKeyword, List<? extends Post> expected) {
        List<Post> actual = postDao.getPlainPostsByKeywordRange(stringToSet(keyword), LIST_SIZE, 0);

        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));

        //assertSortedByName(actual);

        expected = actual.subList(0, 5);
        actual = postDao.getPlainPostsByKeywordRange(stringToSet(keyword), 5, 0);

        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));

        //assertTrue(postDao.getPlainPostsByKeywordRange(stringToSet("sucutrule"), 0, LIST_SIZE).isEmpty());

        assertTrue(postDao.getPlainPostsByKeywordRange(stringToSet(noMatchKeyword), LIST_SIZE, 0).isEmpty());
        PostTestUtils.assertEqualsPlainPosts(PostTestUtils.dummyPost(1),
                postDao.getPlainPostsByKeywordRange(stringToSet("1"), LIST_SIZE, 0).get(0));
    }

    // Pincha para num >10
    private void assertSortedByName(List<? extends Post> actual) {
        for (int i = 1; i < actual.size(); i++) {
            String shouldBeLower = actual.get(i - 1).getTitle();
            String shouldBeHiger = actual.get(i).getTitle();

            assertTrue(shouldBeLower.compareToIgnoreCase(shouldBeHiger) < 0);
        }
    }

    // Se supone que es para la lista reversa pero habria que sortearlo
    private void assertEqualsReversedSortedList(List<Post> expected, List<Post> actual) {
        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            Post expectedPost = expected.get(/*expected.size() - i - 1*/ i);
            Post actualPost = actual.get(i);
            PostTestUtils.assertEqualsFullPosts(expectedPost, actualPost);
            if (i > 1)
                assertTrue(actualPost.getId() > actual.get(i - 1).getId());
        }
    }

    private User insertDummyUser(User dummy) throws DuplicateEmailException {
        return userDao.create(dummy.getName(), dummy.getSurname(), dummy.getMail(), dummy.getPassword(),
                dummy.getProfile_img_url());
    }

    private void insertDummyUsers(List<User> dummyUsers) throws DuplicateEmailException {
        for (int i = 0; i < dummyUsers.size(); i++) {
            User u = dummyUsers.get(i);
            dummyUsers.set(i, insertDummyUser(u));
        }
    }

    private Post insertPost(Post post) throws DuplicateEmailException {
        User u = dummyUserList.get(0);
        return postDao.createPost(post.getTitle(), post.getDescription(), post.getContact_phone(),
                post.getEvent_date(), post.getCategory(), post.getPet(), post.isIs_male(), post.getLocation(),
                u, post.getPostImages());

    }


    private List<Post> insertPosts(List<Post> posts) throws DuplicateEmailException {
        List<Post> inserts = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            Post p = posts.get(i);
            inserts.add(p);
            posts.set(i, insertPost(p));
        }
        return inserts;
    }
}