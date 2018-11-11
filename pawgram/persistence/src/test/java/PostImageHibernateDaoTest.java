import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
import ar.edu.itba.pawgram.interfaces.persistence.PostDao;
import ar.edu.itba.pawgram.interfaces.persistence.UserDao;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.PostImage;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.persistence.PostImageHibernateDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
@Sql("classpath:schema.sql")
public class PostImageHibernateDaoTest {

    private final static String TABLE_NAME = "postImages";
    private final static int DUMMY_LIST_SIZE = 20;

    @Autowired
    private PostImageHibernateDao postImageDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() throws Exception {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");

        insertDummyUser();
        insertDummyPost();
    }

    @Test
    public void getImagesIdByPostIdTest() {
        List<PostImage> expectedList = PostImageTestUtils.dummyPostImageList(DUMMY_LIST_SIZE, 1, 1);

        for (PostImage postImage : expectedList)
            postImageDao.createPostImage(postImage.getId(), "");

        List<PostImage> actualList = postImageDao.getImagesIdByPostId(1);

        int i;
        for (i = 0; i < expectedList.size(); i++)
            assertEquals(expectedList.get(i).getId(), actualList.get(i).getId());

        assertEquals(i, actualList.size());
        assertEquals(0, postImageDao.getImagesIdByPostId(2).size());
        assertEquals(DUMMY_LIST_SIZE, JdbcTestUtils.countRowsInTable(jdbcTemplate, TABLE_NAME));
    }

    @Test
    public void getImageByIdsTest() {
        PostImage expected = PostImageTestUtils.dummyPostImage(1, 1);

        postImageDao.createPostImage(expected.getId(), expected.getUrl());

        List<PostImage> actual = postImageDao.getImagesIdByPostId(1);

        assertEquals(1, actual.size());
        PostImageTestUtils.assertEqualsPostImages(expected, actual.get(0));
    }

    @Test
    public void createPostImageTest() {
        PostImage expected = PostImageTestUtils.dummyPostImage(1, 1);

        PostImage actual = postImageDao.createPostImage( 1, expected.getUrl());

        PostImageTestUtils.assertEqualsPostImages(expected, actual);
    }

    private void insertDummyPost() throws DuplicateEmailException {
        Post dummy = PostTestUtils.dummyPost(1);
        User userdummy = UserTestUtils.dummyUser(0);
        User u = userDao.create(userdummy.getName(), userdummy.getSurname(), userdummy.getMail(), userdummy.getPassword(), userdummy.getProfile_img_url());
        postDao.createPost(dummy.getTitle(), dummy.getDescription(), dummy.getContact_phone(),
                dummy.getEvent_date(), dummy.getCategory(), dummy.getPet(), dummy.isIs_male(), dummy.getLocation(),
                u);
    }

    private void insertDummyUser() throws DuplicateEmailException {
        User dummy = UserTestUtils.dummyUser(1);
        userDao.create(dummy.getName(), dummy.getSurname(), dummy.getMail(), dummy.getPassword(), dummy.getProfile_img_url());
    }
}