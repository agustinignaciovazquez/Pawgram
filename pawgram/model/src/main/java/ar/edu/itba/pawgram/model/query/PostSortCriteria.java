package ar.edu.itba.pawgram.model.query;

import ar.edu.itba.pawgram.model.Post;

import java.util.Comparator;

public enum PostSortCriteria {
    ALPHA((p1, p2) -> p1.getTitle().compareToIgnoreCase(p2.getTitle())),
    ID((p1, p2) -> Long.compare(p1.getId(), p2.getId())),
    DISTANCE((p1, p2) -> Integer.compare(p1.getDistance(), p2.getDistance())),
    DATE((p1, p2) -> p1.getEvent_date().compareTo(p2.getEvent_date()));

    private final Comparator<Post> comparator;

    private PostSortCriteria(final Comparator<Post> comparator) {
        this.comparator = comparator;
    }

    public static PostSortCriteria fromString(final String str) {
        return valueOf(str.toUpperCase());
    }

    public Comparator<Post> getComparator(final OrderCriteria order) {
        return order == OrderCriteria.ASC ? comparator : comparator.reversed();
    }

    public String getLowerName() {
        return this.name().toLowerCase();
    }
}
