package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
import ar.edu.itba.pawgram.interfaces.exception.PostCreateException;
import ar.edu.itba.pawgram.interfaces.persistence.PostDao;
import ar.edu.itba.pawgram.interfaces.persistence.UserDao;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.PostImage;
import ar.edu.itba.pawgram.model.User;
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

import static ar.edu.itba.pawgram.persistence.PostImageTestUtils.assertEqualsPostImages;
import static ar.edu.itba.pawgram.persistence.PostImageTestUtils.dummyPostImage;
import static ar.edu.itba.pawgram.persistence.PostImageTestUtils.dummyPostImageList;
import static ar.edu.itba.pawgram.persistence.PostTestUtils.dummyPost;
import static ar.edu.itba.pawgram.persistence.UserTestUtils.dummyUser;
import static ar.edu.itba.pawgram.persistence.UserTestUtils.profilePictureFromUser;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class PostImageJdbcDaoTest {

    private final static String TABLE_NAME = "postImages";
    private final static int DUMMY_LIST_SIZE = 20;

    @Autowired
    private PostImageJdbcDao postImageDao;

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
        List<PostImage> expectedList = dummyPostImageList(DUMMY_LIST_SIZE, 0, 0);

        for (PostImage postImage : expectedList)
            postImageDao.createPostImage(postImage.getPostId(), postImage.getUrl());

        List<PostImage> actualList = postImageDao.getImagesIdByPostId(0);

        int i;
        for (i = 0; i < expectedList.size(); i++)
            assertEquals(expectedList.get(i).getPostId(), actualList.get(i).getPostId());

        assertEquals(i, actualList.size());
        assertEquals(DUMMY_LIST_SIZE, postImageDao.getImagesIdByPostId(1).size());
        assertEquals(DUMMY_LIST_SIZE, JdbcTestUtils.countRowsInTable(jdbcTemplate, TABLE_NAME));
    }

    //TODO: si implementamos el metodo
//    @Test
//    public void getImageByIdsTest() {
//        PostImage expected = dummyPostImage(0, 0);
//
//        postImageDao.createPostImage(expected.getPostId(), expected.getUrl());
//
//        PostImage actual = postImageDao.getImageByIds(0, 0);
//
//        assertEqualsPostImages(expected, actual);
//    }

    @Test
    public void createPostImageTest() {
        //TODO: check por que inicializa los ids de las inserciones en 1
        PostImage expected = dummyPostImage(1, 0);

        PostImage actual = postImageDao.createPostImage(0, expected.getUrl());

        assertEqualsPostImages(expected, actual);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, TABLE_NAME));
    }

    @Test
    public void onDeleteCascadeTest() {
        PostImage dummy = dummyPostImage(0, 0);
        postImageDao.createPostImage(0, dummy.getUrl());

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, TABLE_NAME));

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "posts");

        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, TABLE_NAME));
    }

    // TODO: check exception y la transformaxcion a raw imgs
    private void insertDummyPost() {
        Post dummy = dummyPost(0);
        List<PostImage> imgs = dummy.getPostImages();
        List<byte[]> raw_imgs = new ArrayList<>();
        for( PostImage pi : imgs){
            raw_imgs.add(pi.getUrl().getBytes());
        }
        try {
            postDao.createPost(dummy.getTitle(), dummy.getDescription(), raw_imgs, dummy.getContact_phone(),
                    dummy.getEvent_date(), dummy.getCategory(), dummy.getPet(), dummy.isMale(), dummy.getLocation(), dummy.getOwner());
        } catch (PostCreateException e) {
            e.printStackTrace();
        }
    }

    private void insertDummyUser() throws DuplicateEmailException {
        User dummy = dummyUser(0);
        userDao.create(dummy.getName(),dummy.getSurname(), dummy.getMail(), dummy.getPassword(), profilePictureFromUser(dummy));
    }
}
