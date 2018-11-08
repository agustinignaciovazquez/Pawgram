package ar.edu.itba.pawgram.model;

import javax.persistence.*;

@Entity
@Table(name = "filedump")
public class FileDump {
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "pathid", length = 64, nullable = false)
    private String pathId;
    @Column(name = "imageid", length = 32, nullable = false)
    private String imageId;
    @Column(nullable = false, columnDefinition = "bytea")
    private byte[] data;

    // Hibernate
    FileDump() {
    }

    public FileDump(String pathId, String imageId, byte[] data) {
        this.pathId = pathId;
        this.imageId = imageId;
        this.data = data;
    }

    public String getPathId() {
        return pathId;
    }

    public String getImageId() {
        return imageId;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof FileDump))
            return false;

        final FileDump other = (FileDump) obj;

        return getImageId().equals(other.getImageId())  && getPathId().equals(other.getPathId());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 17;

        result = result * prime + Integer.valueOf(getPathId());
        result = result * prime + Integer.valueOf(getImageId());;

        return result;
    }

    @Override
    public String toString() {
        return "Image " + getImageId() + " from path " + getPathId();
    }
}
