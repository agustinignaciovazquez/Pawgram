package ar.edu.itba.pawgram.webapp.form;

import ar.edu.itba.pawgram.model.Location;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SearchZoneForm {
    private static final int DEFAULT_RANGE_KM = 3;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;

    @NotNull
    @Range(min = 1, max = 20)
    private Integer range = DEFAULT_RANGE_KM;

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

    public Integer getRange() {
        return range;
    }
    public Integer getRangeInMeters(){
        return range*1000;
    }
    public void setRange(Integer range) {
        this.range = range;
    }
}
