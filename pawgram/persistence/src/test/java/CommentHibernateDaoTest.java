import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
import ar.edu.itba.pawgram.interfaces.persistence.PostDao;
import ar.edu.itba.pawgram.interfaces.persistence.UserDao;
import ar.edu.itba.pawgram.model.Comment;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.persistence.CommentHibernateDao;
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
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
@Sql("classpath:schema.sql")
public class CommentHibernateDaoTest {

    @Autowired
    private CommentHibernateDao commentDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;
    private User userDummy;

    @Before
    public void setUp() throws Exception {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");

        userDummy = insertDummyUser();
        insertDummyPost();
    }

    /*
    @Test
    public void createParentCommentTest() {
        Comment expected = CommentTestUtils.dummyParentComment(1, 1, 1);
        expected = insertComment(expected);
        Comment actual = expected;

        CommentTestUtils.assertEqualsComments(expected, actual);
        assertFalse(actual.hasParent());
        CommentTestUtils.assertEqualsComments(actual, commentDao.getCommentById(actual.getId()));
    }
    */

    /*
    @Test
    public void createChildCommentTest() {
        Comment dummyParent = CommentTestUtils.dummyParentComment(1, 1, 1);
        insertComment(dummyParent).getId();
        Comment expected = CommentTestUtils.dummyComment(2, dummyParent, 1, 1);
        expected = insertComment(expected);
        Comment actual = insertComment(expected);

        CommentTestUtils.assertEqualsComments(expected, actual);
        assertTrue(actual.hasParent());
        CommentTestUtils.assertEqualsComments(dummyParent, actual.getParent());
        CommentTestUtils.assertEqualsComments(actual, commentDao.getCommentById(actual.getId()));
    }
    */

    @Test
    public void getCommentsByPostIdTest() {
        List<Comment> parentCommentList = CommentTestUtils.dummyParentCommentList(7, 1, 1, 1);
        insertCommentList(parentCommentList, 1);

        for (int i = 0; i < 7; i++)
            insertCommentList(CommentTestUtils.dummyCommentList(5, 7 + i * 5, parentCommentList.get(i), 1, 1), 1);

        List<Comment> actual = commentDao.getCommentsByPostId(1);
        List<Comment> comments = new ArrayList<>(actual.size());
        for (Comment comment : actual)
            comments.add(comment);

        assertCommentsOrder(comments);
    }

    private void insertCommentList(List<Comment> comments, int postId) {
        for (Comment comment : comments)
            insertComment(comment);
    }

    // Asserts that a block of parent comments come first, then a block of child
    // comments with parentId of the first parent comment and so on
    private void assertCommentsOrder(List<Comment> comments) {
        assertFalse(comments.get(0).hasParent());

        for (int i = 0, j = childrenPointer(comments); j < comments.size(); j++) {
            Comment parent = comments.get(i);
            Comment child = comments.get(j);

            assertFalse(parent.hasParent());

            if (child.getParent().getId() > parent.getId()) {
                i++;
            } else {
                assertEquals(parent.getId(), child.getParent().getId());
                if (j < comments.size() - 1 && child.getParent().getId() == comments.get(j + 1).getParent().getId())
                    assertTrue(child.getCommentDate().compareTo(comments.get(j + 1).getCommentDate()) < 0); // Oldest
                // comments
                // first
            }
        }
    }

    private int childrenPointer(List<Comment> comments) {
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).hasParent())
                return i;

            if (i < comments.size() - 1 && comments.get(i + 1).hasParent())
                assertTrue(comments.get(i).getCommentDate().compareTo(comments.get(i + 1).getCommentDate()) < 0); // Oldest
            // comments
            // first
        }

        return comments.size();
    }

    private Comment insertComment(Comment comment) {
        if (comment.hasParent())
            return commentDao.createComment(comment.getContent(), comment.getCommentDate(), comment.getParent(),
                    comment.getCommentedPost(), userDummy);
        return commentDao.createParentComment(comment.getContent(), comment.getCommentDate(),
                comment.getCommentedPost(), userDummy);
    }

    private void insertDummyPost() throws DuplicateEmailException {
        Post dummy = PostTestUtils.dummyPost(1);
        User userdummy = UserTestUtils.dummyUser(0);
        User u = userDao.create(userdummy.getName(), userdummy.getSurname(), userdummy.getMail(), userdummy.getPassword(), userdummy.getProfile_img_url());
        postDao.createPost(dummy.getTitle(), dummy.getDescription(), dummy.getContact_phone(),
                dummy.getEvent_date(), dummy.getCategory(), dummy.getPet(), dummy.isIs_male(), dummy.getLocation(),
                userDummy);
    }

    private User insertDummyUser() throws DuplicateEmailException {
        User dummy = UserTestUtils.dummyUser(1);
        return userDao.create(dummy.getName(), dummy.getSurname(), dummy.getMail(), dummy.getPassword(), dummy.getProfile_img_url());
    }
}
