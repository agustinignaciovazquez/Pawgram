package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
import ar.edu.itba.pawgram.interfaces.exception.PostCreateException;
import ar.edu.itba.pawgram.interfaces.persistence.UserDao;
import ar.edu.itba.pawgram.model.Category;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.PostImage;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static ar.edu.itba.pawgram.persistence.PostTestUtils.*;
import static ar.edu.itba.pawgram.persistence.UserTestUtils.dummyUserList;
import static ar.edu.itba.pawgram.persistence.UserTestUtils.profilePictureFromUser;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class PostJdbcDaoTest {

    private static final int LIST_SIZE = Category.values().length * 5;

    @Autowired
    private PostJdbcDao postDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() throws Exception {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
        insertDummyUser();
    }

    @Test
    public void getPostsTest() {
        List<Post> expected = dummyPostList(LIST_SIZE, 0);
        insertPosts(expected);

        //TODO: FIX
        List<PlainPost> actual = postDao.getPlainPostsRange(0, 0);

        assertEqualsReversedSortedList(expected, actual);

        assertEquals(LIST_SIZE, JdbcTestUtils.countRowsInTable(jdbcTemplate, "posts"));
    }

    @Test
    public void getPostsByCategoryTest() {
        Category[] categories = Category.values();
        List<Post> postList = dummyPostList(LIST_SIZE, 0);
        insertPosts(postList);

        for (int i = 0; i < categories.length; i++)
            assertRetrievedCategory(categories[i], postList);

        assertEquals(LIST_SIZE, JdbcTestUtils.countRowsInTable(jdbcTemplate, "posts"));
    }

    @Test
    public void createPostTest() {
        Post expected = dummyPost(0);
        Post actual = insertPost(expected);

        assertEqualsFullPosts(expected, actual);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "posts"));
    }

    @Test
    public void getPostByPostIdTest() {
        Post expected = dummyPost(0);
        insertPost(expected);

        Post actual = postDao.getFullPostById(0).build();

        assertEqualsFullPosts(expected, actual);
        assertNull(postDao.getFullPostById(1));
    }

    @Test
    public void getPlainPostByUserIdTest() {
        List<Post> expected = dummyPostListWithUserId(LIST_SIZE, 0, 0);
        insertPosts(expected);

        //TODO:Chequear limit y offset
        List<PlainPost> actual = postDao.getPlainPostsByUserIdRange(0, 0 ,0);

        assertEqualsReversedSortedList(expected, actual);

        assertEquals(LIST_SIZE, JdbcTestUtils.countRowsInTable(jdbcTemplate, "posts"));
    }


    @Test
    public void deletePostByUserId() {
        Post dummyPost = dummyPost(0);
        insertPost(dummyPost);

        assertTrue(postDao.deletePostById(0));
        assertFalse(postDao.deletePostById(0));

        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "posts"));
    }

    @Test
    public void getPlainPostsByKeywordMatchNameTest() {
        List<Post> expected = dummyPostList(LIST_SIZE, 0);
        insertPosts(expected);
        String keyword = expected.get(0).getTitle().substring(0, 3);
        String noMatchKeyword = expected.get(0).getTitle().substring(1, 3);

        assertSearch(keyword, noMatchKeyword, expected);
    }

    @Test
    public void getPlainPostsByKeywordMatchTaglineTest() {
        List<Post> expected = dummyPostList(LIST_SIZE, 0);
        insertPosts(expected);
        String keyword = expected.get(0).getDescription().substring(0, 3);
        String noMatchKeyword = expected.get(0).getDescription().substring(1, 3);

        assertSearch(keyword, noMatchKeyword, expected);
        List<PlainPost> actual = postDao.getPlainPostsByKeywordRange("desc", LIST_SIZE, 0);
        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));
    }

    private void assertSearch(String keyword, String noMatchKeyword, List<? extends PlainPost> expected) {
        List<PlainPost> actual = postDao.getPlainPostsByKeywordRange(keyword, LIST_SIZE, 0);

        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));

        assertSortedByName(actual);

        expected = actual.subList(0, 5);
        actual = postDao.getPlainPostsByKeywordRange(keyword, 5, 0);

        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));

        assertTrue(postDao.getPlainPostsByKeywordRange("random", LIST_SIZE, 0).isEmpty());

        assertTrue(postDao.getPlainPostsByKeywordRange(noMatchKeyword, LIST_SIZE, 0).isEmpty());
        assertEqualsPlainPosts(dummyPost(0), postDao.getPlainPostsByKeywordRange("0", LIST_SIZE, 0).get(0));
    }

    private void assertSortedByName(List<? extends PlainPost> actual) {
        for (int i = 1; i < actual.size(); i++) {
            String shouldBeLower = actual.get(i-1).getTitle();
            String shouldBeHiger = actual.get(i).getTitle();
            assertTrue(shouldBeLower.compareToIgnoreCase(shouldBeHiger) < 0);
        }
    }

    private void insertDummyUser() throws DuplicateEmailException {
        List<User> list = dummyUserList(LIST_SIZE, 0);
        for (User dummy : list)
            userDao.create(dummy.getName(),dummy.getSurname(),  dummy.getMail(), dummy.getPassword(), profilePictureFromUser(dummy));
    }

    //TODO: este fix me parecio medio cabeza
    private Post insertPost(Post post) {
        List<PostImage> imgs = post.getPostImages();
        List<byte[]> raw_imgs = new ArrayList<>();
        for( PostImage pi : imgs){
            raw_imgs.add(pi.getUrl().getBytes());
        }
        try {
            return postDao.createPost(post.getTitle(), post.getDescription(), raw_imgs, post.getContact_phone(),
                    post.getEvent_date(), post.getCategory(), post.getPet(), post.isMale(), post.getLocation(), post.getOwner()).build();
        } catch (PostCreateException e) {
            //TODO: que hacemos con la excep?
            e.printStackTrace();
            return null;
        }
    }

    private void insertPosts(List<Post> posts) {
        for(Post post : posts)
            insertPost(post);
    }

    private void assertEqualsReversedSortedList(List<? extends PlainPost> expected, List<? extends PlainPost> actual) {
        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            PlainPost expectedPost = expected.get(expected.size()-i-1);
            PlainPost actualPost = actual.get(i);
            assertEquals(expectedPost, actualPost);
            if (i > 0)
                assertTrue(actualPost.getId() < actual.get(i-1).getId());
        }
    }

    private void assertRetrievedCategory(Category category, List<? extends PlainPost> postList) {
        List<PlainPost> postsByCategory = postDao.getPlainPostsByCategoryRange(category, 0, 0);

        for (PlainPost post : postsByCategory) {
            assertTrue(postList.contains(post));
            assertEquals(category, post.getCategory());
        }

        assertEquals(LIST_SIZE / Category.values().length, postsByCategory.size());
    }
}
