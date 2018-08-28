package ar.edu.itba.pawgram.model;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notEmpty;

public abstract class Image {
    private final long id;
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
