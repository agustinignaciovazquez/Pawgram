package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
import ar.edu.itba.pawgram.interfaces.exception.InvalidCommentException;
import ar.edu.itba.pawgram.interfaces.exception.PostCreateException;
import ar.edu.itba.pawgram.interfaces.persistence.PostDao;
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
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ar.edu.itba.pawgram.persistence.CommentTestUtils.*;
import static ar.edu.itba.pawgram.persistence.PostTestUtils.dummyPost;
import static ar.edu.itba.pawgram.persistence.UserTestUtils.dummyUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class CommentJdbcDaoTest {

    @Autowired
    private CommentJdbcDao commentDao;

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
    public void createParentCommentTest() {
        Comment expected = dummyParentComment(0, 0);
        Comment actual = insertComment(expected, 0);

        assertEqualsComments(expected, actual);
        assertFalse(actual.hasParent());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "comments"));
    }

    @Test
    public void createChildCommentTest() {
        insertComment(dummyParentComment(0, 0), 0).getId();
        Comment expected = dummyComment(1, 0, 0);
        Comment actual = insertComment(expected, 0);

        assertEqualsComments(expected, actual);
        assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "comments"));
    }

    @Test
    public void getCommentsByPostIdTest() {
        insertCommentList(dummyParentCommentList(7, 0, 0), 0);

        for (int i = 0; i < 7; i++)
            insertCommentList(dummyCommentList(5, 7 + i * 5, i, 0), 0);

        List<Comment> actual = commentDao.getCommentsByPostId(0);
        List<Comment> comments = new ArrayList<>(actual.size());
        for (Comment comment : actual)
            comments.add(comment);

        assertCommentsOrder(comments);
    }

    private void insertCommentList(List<Comment> comments, int postId) {
        for (Comment comment : comments)
            insertComment(comment, postId);
    }

    // Asserts that a block of parent comments come first, then a block of child comments with parentId of the first parent comment and so on
    private void assertCommentsOrder(List<Comment> comments) {
        assertFalse(comments.get(0).hasParent());

        for (int i = 0, j = childrenPointer(comments); j < comments.size(); j++) {
            Comment parent = comments.get(i);
            Comment child = comments.get(j);

            assertFalse(parent.hasParent());

            if (child.getParentId() > parent.getId()) {
                i++;
            }
            else {
                assertEquals(parent.getId(), child.getParentId());
                if (j < comments.size() - 1 && child.getParentId() == comments.get(j+1).getParentId())
                    assertTrue(child.getCommentDate().compareTo(comments.get(j+1).getCommentDate()) < 0); // Oldest comments first
            }
        }
    }

    private int childrenPointer(List<Comment> comments) {
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).hasParent())
                return i;

            if (i < comments.size()-1 && comments.get(i+1).hasParent())
                assertTrue(comments.get(i).getCommentDate().compareTo(comments.get(i+1).getCommentDate()) < 0); // Oldest comments first
        }

        return comments.size();
    }

    private Comment insertComment(Comment comment, int postId) {
        if (comment.hasParent()) {
            try {
                return commentDao.createComment(comment.getContent(), comment.getCommentDate(), comment.getParentId(), postId, comment.getAuthor().getId());
            } catch (InvalidCommentException e) {
                //e.printStackTrace();
            }
        }
        return commentDao.createParentComment(comment.getContent(), comment.getCommentDate(), postId, comment.getAuthor().getId());
    }

    private void insertDummyPost() throws PostCreateException {
        Post dummy = dummyPost(0);
        String now = "2016-11-09 10:30";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        LocalDateTime formatDateTime = LocalDateTime.parse(now, formatter);
        postDao.createPost("title","description",null,"011-4251-1695",
                formatDateTime, Category.ADOPT, Pet.DOG,true,new Location(0,0),dummyUser(0));
    }

    private void insertDummyUser() throws DuplicateEmailException {
        User dummy = dummyUser(0);
        userDao.create(dummy.getName(),dummy.getSurname(),dummy.getMail(),dummy.getPassword(),dummy.getProfile_img_url());
    }

}
