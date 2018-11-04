package ar.edu.itba.pawgram.model.interfaces;

import ar.edu.itba.pawgram.model.structures.Location;

public interface PlainSearchZone {
    public long getId();
    public Location getLocation();
    public int getRange();
}
