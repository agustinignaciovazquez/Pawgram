package ar.edu.itba.pawgram.model;

import ar.edu.itba.pawgram.model.interfaces.PlainPost;

import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

public class SearchZone {
    private long id;
    private Location location;
    private int range;
    private List<PlainPost> posts;
    private User user;

    public static SearchZoneBuilder getBuilder(long id, Location location, int range) {
        isTrue(id >= 0, "SearchZone ID must be non negative: %d", id);
        isTrue(range >= 0, "SearchZone range must be non negative: %d", id);
        notNull(location, "Location must not be null");

        return new SearchZoneBuilder(id, location, range);
    }

    private SearchZone(SearchZoneBuilder builder) {
        this.id = builder.id;
        this.location = builder.location;
        this.range = builder.range;
        this.posts = builder.posts;
        this.user = builder.user;
    }

    public long getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public int getRange() {
        return range;
    }

    public List<PlainPost> getPosts() {
        return posts;
    }

    public User getUser() {
        return user;
    }

    public static class SearchZoneBuilder{
        private long id;
        private Location location;
        private int range;
        private User user;
        private List<PlainPost> posts = Collections.emptyList();

        private SearchZoneBuilder(long id, Location location, int range) {
            this.id = id;
            this.location = location;
            this.range = range;
        }

        public SearchZoneBuilder user(User user) {
            this.user = user;
            return this;
        }

        public SearchZoneBuilder posts(List<PlainPost> posts) {
            this.posts = posts;
            return this;
        }

        public Location getLocation() {
            return location;
        }

        public int getRange() {
            return range;
        }

        public SearchZone build() {
            return new SearchZone(this);
        }
    }

}
