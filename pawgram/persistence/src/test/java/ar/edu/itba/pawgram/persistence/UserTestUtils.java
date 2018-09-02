package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.model.User;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public final class UserTestUtils {

    private UserTestUtils() {
    }

    public static User dummyUser(int id) {
        return new User(id, "Agus","Vazquez",  "agvazquez" + id + "@itba.edu.ar", "molly" + id, null);
    }

    public static List<User> dummyUserList(int size, int initialId) {
        List<User> userList = new ArrayList<User>(size);

        for (int i = 0; i < size; i++)
            userList.add(dummyUser(initialId + i));

        return userList;
    }

    public static void assertEqualsUsers(User expected, User actual) {
        assertEquals(expected, actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getMail(), actual.getMail());
    }

    //TODO: Correct - podemos devolver una url que cambie segun el mail o algo asi
    public static String profilePictureFromUser(User dummyUser) {
        return dummyUser.getMail().getBytes().toString();
    }
}
