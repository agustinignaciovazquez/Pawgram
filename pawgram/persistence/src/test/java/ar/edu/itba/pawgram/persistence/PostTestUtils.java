package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.model.Category;
import ar.edu.itba.pawgram.model.Location;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.PostImage;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ar.edu.itba.pawgram.persistence.UserTestUtils.dummyUser;
import static org.junit.Assert.assertEquals;

public final class PostTestUtils {

    private PostTestUtils() {
    }

    public static Post dummyPost(int id) {
        return dummyPostBuilder(id).build();
    }

    public static Post dummyPostWithCategory(int id, Category category) {
        return postBuilder(id).category(category).build();
    }

    public static Post.PostBuilder dummyPostBuilder(int id) {
        Category[] categories = Category.values();
        int len = categories.length;

        return postBuilder(id).category(categories[id % len]);
    }

    //TODO: chequear si hay que llenar todos los campos
    private static Post.PostBuilder postBuilder(int id) {
        PostImage image = new PostImage(id,"TITLE", id);
        List<PostImage> postImages = new ArrayList<>();
        postImages.add(image);
        return Post.getBuilder(id, "Post " + id, postImages)
                .description("Description " + id)
                .user(dummyUser(id))
                .event_date(LocalDateTime.now().plusSeconds(id))
                .pet("OTHER") // TODO: make dummy
                .location(new Location(0,0))
                .contact_phone("1168218680");
    }

    public static List<Post> dummyPostList(int size, int initialId) {
        List<Post> postList = new ArrayList<>(size);

        for (int i = 0; i < size; i++)
            postList.add(dummyPost(initialId + i));

        return postList;
    }

    public static List<PlainPost> dummyPlainPostList(int size, int initialId) {
        List<PlainPost> postList = new ArrayList<>(size);

        for (int i = 0; i < size; i++)
            postList.add(dummyPost(initialId + i));

        return postList;
    }

    public static List<Post> dummyPostListWithUserId(int size, int initialId, int userId) {
        List<Post> postList = new ArrayList<>(size);

        for (int i = 0; i < size; i++)
            postList.add(dummyPostBuilder(initialId + i).user(dummyUser(userId)).build());

        return postList;
    }

    public static List<PlainPost> dummyPlainPostListWithUserId(int size, int initialId, int userId) {
        List<PlainPost> postList = new ArrayList<>(size);

        for (int i = 0; i < size; i++)
            postList.add(dummyPostBuilder(initialId + i).user(dummyUser(userId)).build());

        return postList;
    }

    public static List<PlainPost> dummyPlainPostListWithCategory(int size, int initialId, Category category) {
        List<PlainPost> postList = new ArrayList<>(size);

        for (int i = 0; i < size; i++)
            postList.add(dummyPostWithCategory(initialId + i, category));

        return postList;
    }


    public static void assertEqualsFullPosts(Post expected, Post actual) {
        assertEquals(expected, actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        //TODO: las imgs no se guardan en la base de datos para los post
        //assertEquals(expected.getPostImages(), actual.getPostImages());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getCategory(), actual.getCategory());
    }

    public static void assertEqualsPlainPosts(PlainPost expected, PlainPost actual) {
        assertEquals(expected, actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getCategory(), actual.getCategory());
    }
}

