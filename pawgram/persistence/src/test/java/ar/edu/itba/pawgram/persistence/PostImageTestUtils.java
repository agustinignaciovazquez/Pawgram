package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.model.PostImage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public final class PostImageTestUtils {

    private PostImageTestUtils() {
    }

    //TODO: put valid test url ?
    public static PostImage dummyPostImage(int id, int postId) {
        String url = Integer.toString(id + postId);
        return new PostImage(id, url , postId);
    }

    public static List<PostImage> dummyPostImageList(int size, int initialImageId, int postId) {
        List<PostImage> postImageList = new ArrayList<PostImage>(size);

        for (int i = 0; i < size; i++)
            postImageList.add(dummyPostImage(initialImageId + i, postId));

        return postImageList;
    }

    public static void assertEqualsPostImages(PostImage expected, PostImage actual) {
        assertEquals(expected, actual);
        assertEquals(expected.getPostId(), expected.getPostId());
        assertEquals(expected.getPostId(), expected.getPostId());
        assertEquals(expected.getUrl(), expected.getUrl());
    }
}