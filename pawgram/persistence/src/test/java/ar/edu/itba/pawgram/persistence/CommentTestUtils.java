package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.model.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ar.edu.itba.pawgram.persistence.UserTestUtils.assertEqualsUsers;
import static ar.edu.itba.pawgram.persistence.UserTestUtils.dummyUser;
import static org.junit.Assert.assertEquals;

public final class CommentTestUtils {

    private CommentTestUtils() {
    }

    public static Comment dummyParentComment(int commentId, int authorId) {
        return new Comment(commentId, dummyUser(authorId), "Content " + commentId, LocalDateTime.now().plusSeconds(commentId));
    }

    public static Comment dummyComment(int commentId, int parentId, int authorId) {
        return new Comment(commentId, parentId, dummyUser(authorId), "Content " + commentId, LocalDateTime.now().plusSeconds(commentId));
    }

    public static List<Comment> dummyParentCommentList(int size, int initialId, int authorId) {
        List<Comment> list = new ArrayList<>(size);

        for (int i = 0; i < size; i++)
            list.add(dummyParentComment(initialId + i, authorId));

        return list;
    }

    public static List<Comment> dummyCommentList(int size, int initialId, Integer parentId, int authorId) {
        List<Comment> list = new ArrayList<>(size);

        for (int i = 0; i < size; i++)
            list.add(dummyComment(initialId + i, parentId, authorId));

        return list;
    }

    public static void assertEqualsComments(Comment expected, Comment actual) {
        assertEqualsUsers(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.hasParent(), actual.hasParent());
        if (expected.hasParent())
            assertEquals(expected.getParentId(), actual.getParentId());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getContent(), actual.getContent());
        assertEquals(expected, actual);
    }
}
