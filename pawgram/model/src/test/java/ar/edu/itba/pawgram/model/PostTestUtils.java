package ar.edu.itba.pawgram.model;
import ar.edu.itba.pawgram.model.structures.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public final class PostTestUtils {

    private PostTestUtils() {
    }

    public static Post dummyPost(int id) {
        return dummyPostBuilder(id).build();
    }

    public static Post dummyPostWithUserId(int id, int userId) {
        return dummyPostBuilder(id).user(UserTestUtils.dummyUser(userId)).build();
    }

    public static Post dummyPostWithCategory(int id, Category category) {
        return postBuilder(id).category(category).build();
    }

    public static Post.PostBuilder dummyPostBuilder(int id) {
        Category[] categories = Category.values();
        int len = categories.length;

        return postBuilder(id).category(categories[id % len]);
    }

    private static Post.PostBuilder postBuilder(int id) {
        String title = "Post " + id;
        Date date = new Date();
        List<PostImage> postImages = new ArrayList<>();
        postImages.add(new PostImage("http://www.postseek.com/",id));
        Location loc = new Location(10,10);
        return Post.getBuilder(title, postImages).id(id).description("Description " + id).postImages(postImages)
                .contact_phone("phone " + id).user(null).pet(Pet.DOG).location(loc).is_male(true)
                .event_date(date).distance(0);
    }

    public static List<Post> dummyPostList(int size, int initialId) {
        List<Post> postList = new ArrayList<>(size);

        for (int i = 0; i < size; i++)
            postList.add(dummyPost(initialId + i));

        return postList;
    }

    public static List<Post> dummyPlainPostList(int size, int initialId) {
        List<Post> postList = new ArrayList<>(size);

        for (int i = 0; i < size; i++)
            postList.add(dummyPost(initialId + i));

        return postList;
    }

    public static List<Post> dummyPostListWithUserId(int size, int initialId, int userId) {
        List<Post> postList = new ArrayList<>(size);

        for (int i = 0; i < size; i++)
            postList.add(dummyPostWithUserId(initialId + i, userId));

        return postList;
    }

    public static List<Post> dummyPlainPostListWithUserId(int size, int initialId, int userId) {
        List<Post> postList = new ArrayList<>(size);

        for (int i = 0; i < size; i++)
            postList.add(dummyPostBuilder(initialId + i).user(UserTestUtils.dummyUser(userId)).build());

        return postList;
    }

    public static List<Post> dummyPlainPostListWithCategory(int size, int initialId, Category category) {
        List<Post> postList = new ArrayList<>(size);

        for (int i = 0; i < size; i++)
            postList.add(dummyPostWithCategory(initialId + i, category));

        return postList;
    }

    public static void assertEqualsFullPosts(Post expected, Post actual) {
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getCategory(), actual.getCategory());
        assertEquals(expected.getOwner(), actual.getOwner());
        assertEquals(expected, actual);
    }

    public static void assertEqualsPlainPosts(Post expected, Post actual) {
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getCategory(), actual.getCategory());
        assertEquals(expected, actual);
    }

    public static byte[] logoFromPost(Post post) {
        return post.getTitle().getBytes();
    }
}