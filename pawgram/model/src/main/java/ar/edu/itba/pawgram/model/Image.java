package ar.edu.itba.pawgram.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notEmpty;
@MappedSuperclass
public abstract class Image {
    @Id
    @GeneratedValue
    private final long id;
    @Column(length = 32, nullable = false)
    private final String url;

    public Image(long id, String url) {
        isTrue(id >= 0, "Image ID must be non negative: %d", id);
        this.id = id;
        this.url = notEmpty(url);
    }

    public long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
