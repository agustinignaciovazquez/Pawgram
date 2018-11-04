package ar.edu.itba.pawgram.model.structures;

import javax.persistence.Embeddable;

@Embeddable
public class Location {
    private double longitude;
    private double latitude;

    public Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Location))
            return false;

        Location other = (Location) obj;

        return (longitude == other.getLongitude() && latitude == other.getLatitude());
    }

    @Override
    public String toString() {
        return latitude + " " + longitude;
    }
}
