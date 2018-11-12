package ar.edu.itba.pawgram.model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public final class UserTestUtils {

    private UserTestUtils() {
    }

    public static User dummyUser(int id) {
        String email = "agvazquez" + id + "@itba.edu.ar";
        return new User( "Agustin","Vazquez", email, "barbacoa", new String(email));
    }

    public static List<User> dummyUserList(int size, int initialId) {
        List<User> userList = new ArrayList<User>(size);

        for (int i = 0; i < size; i++)
            userList.add(dummyUser(initialId + i));

        return userList;
    }

    public static void assertEqualsUsers(User expected, User actual) {
        assertEquals(expected, actual);
        //assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getMail(), actual.getMail());
        assertEquals(expected.getProfile_img_url(), actual.getProfile_img_url());
    }

}
