package ar.edu.itba.pawgram.model.comparator;

import ar.edu.itba.pawgram.model.Post;

import java.util.Comparator;

public class PostDistanceComparator implements Comparator<Post> {

    @Override
    public int compare(final Post o1, final Post o2) {
        int cmp = Integer.compare(o1.getDistance(),o2.getDistance());

        return cmp != 0 ? cmp : Long.compare(o1.getId(), o2.getId());
    }

}