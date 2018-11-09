package ar.edu.itba.pawgram.model;

import ar.edu.itba.pawgram.model.structures.Location;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;
@Entity
@Table(name = "search_zones")
public class SearchZone {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "search_zone_zoneid_seq")
    @SequenceGenerator(sequenceName = "search_zone_zoneid_seq", name = "search_zone_zoneid_seq", allocationSize = 1)
    @Column(name = "zoneid")
    private long id;
    @Embedded
    private Location location;
    @Column(name = "range", nullable = false)
    private int range;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userid", nullable = false, updatable = false)
    private User user;
    @Transient
    private List<Post> posts;
    @Transient
    private long max_page;

    public static SearchZoneBuilder getBuilder(Location location, int range) {
        isTrue(range >= 0, "SearchZone range must be non negative: %d", range);
        notNull(location, "Location must not be null");

        return new SearchZoneBuilder( location, range);
    }

    public static SearchZoneBuilder getBuilderFromSearchZone(final SearchZone sz) {
        notNull(sz, "Search zone cannot be null in order to retrieve Builder");

        return new SearchZoneBuilder(sz);
    }
    //Hibernate
    SearchZone(){
    }

    private SearchZone(SearchZoneBuilder builder) {
        this.id = builder.id;
        this.location = builder.location;
        this.range = builder.range;
        this.posts = builder.posts;
        this.user = builder.user;
        this.max_page = builder.max_page;
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

    public List<Post> getPosts() {
        return posts;
    }

    public long getMax_page() { return max_page; }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof SearchZone))
            return false;

        SearchZone other = (SearchZone) obj;

        return id == other.getId();
    }

    @Override
    public int hashCode() {
        return (int)id;
    }

    @Override
    public String toString() {
        return location.toString() + " " + range;
    }

    public static class SearchZoneBuilder{
        private long id;
        private Location location;
        private int range;
        private User user;
        private List<Post> posts = Collections.emptyList();
        private long max_page;

        private SearchZoneBuilder(Location location, int range) {
            this.location = location;
            this.range = range;
        }

        private SearchZoneBuilder(SearchZone sz){
            this.id = sz.id;
            this.location = sz.location;
            this.range = sz.range;
            this.user = sz.user;
            this.posts = sz.posts;
            this.max_page = sz.max_page;
        }

        public SearchZoneBuilder id(long id) {
            isTrue(id >= 0, "SearchZone ID must be non negative: %d", id);
            this.id = id;
            return this;
        }

        public SearchZoneBuilder user(User user) {
            this.user = user;
            return this;
        }

        public SearchZoneBuilder posts(List<Post> posts) {
            this.posts = posts;
            return this;
        }

        public SearchZoneBuilder max_page(long max_page) {
            this.max_page = max_page;
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
