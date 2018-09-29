package ar.edu.itba.pawgram.webapp.form;

import ar.edu.itba.pawgram.model.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SearchZoneForm {
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    @NotNull
    @Size(min = 1, max = 20)
    private int range;

    @NotNull
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(@NotNull Double latitude) {
        this.latitude = latitude;
    }

    @NotNull
    public Double getLongitude() {
        return longitude;
    }

    public Location getLocation(){
        return new Location(longitude,latitude);
    }

    public void setLongitude(@NotNull Double longitude) {
        this.longitude = longitude;
    }

    public int getRange() {
        return range;
    }
    public int getRangeInMeters(){
        return range*1000;
    }
    public void setRange(int range) {
        this.range = range;
    }
}
