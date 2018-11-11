package ar.edu.itba.pawgram.model.comparator;

import ar.edu.itba.pawgram.model.User;

import java.util.Comparator;

public class UserAlphaComparator implements Comparator<User> {

    @Override
    public int compare(final User o1, final User o2) {
        int cmp = o1.getName().compareToIgnoreCase(o2.getName());

        return cmp != 0 ? cmp : Long.compare(o1.getId(), o2.getId());
    }

}
