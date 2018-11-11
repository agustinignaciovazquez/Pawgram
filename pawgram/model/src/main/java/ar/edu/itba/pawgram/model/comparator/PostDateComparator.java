package ar.edu.itba.pawgram.model.comparator;

import ar.edu.itba.pawgram.model.Post;

import java.util.Comparator;

public class PostDateComparator implements Comparator<Post> {

    @Override
    public int compare(final Post o1, final Post o2) {
        int cmp = o1.getEvent_date().compareTo(o2.getEvent_date());

        return cmp != 0 ? cmp : Long.compare(o1.getId(), o2.getId());
    }

}