package ar.edu.itba.pawgram.model.comparator;

import ar.edu.itba.pawgram.model.Post;

import java.util.Comparator;

public class PostAlphaComparator implements Comparator<Post> {

    @Override
    public int compare(final Post o1, final Post o2) {
        int cmp = o1.getTitle().compareToIgnoreCase(o2.getTitle());

        return cmp != 0 ? cmp : Long.compare(o1.getId(), o2.getId());
    }

}
