package ar.edu.itba.pawgram.webapp.dto.form;

import ar.edu.itba.pawgram.interfaces.service.SearchZoneService;
import ar.edu.itba.pawgram.model.structures.Location;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

public class FormSearchZone {
    private static final int DEFAULT_RANGE_KM = 3;
    private static final int MAX_RANGE_KM = SearchZoneService.MAX_RANGE_KM;
    private static final int MIN_RANGE_KM = SearchZoneService.MIN_RANGE_KM;

    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;

    @NotNull
    @Range(min = MIN_RANGE_KM, max = MAX_RANGE_KM)
    private Integer range = DEFAULT_RANGE_KM;

    public static int getDefaultRangeKm() {
        return DEFAULT_RANGE_KM;
    }

    public static int getMaxRangeKm() {
        return MAX_RANGE_KM;
    }

    public static int getMinRangeKm() {
        return MIN_RANGE_KM;
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

    public Integer getRange() {
        return range;
    }

    public void setRange(Integer range) {
        this.range = range;
    }

    public Location getLocation(){
        return new Location(longitude,latitude);
    }
}
