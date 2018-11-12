package ar.edu.itba.pawgram.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public final class CommentTestUtils {

    private CommentTestUtils() {
    }

    public static Comment dummyParentComment(int commentId, int authorId, int postId) {
        LocalDateTime ldt = LocalDateTime.now().plusSeconds(commentId);
        return new Comment(UserTestUtils.dummyUser(authorId), PostTestUtils.dummyPost(postId), "Content " + commentId,
                Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant())).setCommentId(commentId);
    }

    public static Comment dummyComment(int commentId, Comment parent, int authorId, int postId) {
        LocalDateTime ldt = LocalDateTime.now().plusSeconds(commentId);
        return new Comment(parent, UserTestUtils.dummyUser(authorId), PostTestUtils.dummyPost(postId), "Content " + commentId,
                Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant())).setCommentId(commentId);
    }

    public static List<Comment> dummyParentCommentList(int size, int initialId, int authorId, int postId) {
        List<Comment> list = new ArrayList<>(size);

        for (int i = 0; i < size; i++)
            list.add(dummyParentComment(initialId + i, authorId, postId));

        return list;
    }

    public static List<Comment> dummyCommentList(int size, int initialId, Comment parent, int authorId, int postId) {
        List<Comment> list = new ArrayList<>(size);

        for (int i = 0; i < size; i++)
            list.add(dummyComment(initialId + i, parent, authorId, postId));

        return list;
    }

    public static void assertEqualsComments(Comment expected, Comment actual) {
        UserTestUtils.assertEqualsUsers(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.hasParent(), actual.hasParent());
        if (expected.hasParent())
            assertEquals(expected.getParent(), actual.getParent());
        assertEquals(expected.getContent(), actual.getContent());
    }
}
