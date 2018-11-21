package ar.edu.itba.pawgram.webapp.dto;

import ar.edu.itba.pawgram.model.structures.Location;

import java.net.URI;

public class LocationDTO {
    private Double latitude;
    private Double longitude;

    public LocationDTO(){

    }

    public LocationDTO(final Location location, final URI baseUri) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
