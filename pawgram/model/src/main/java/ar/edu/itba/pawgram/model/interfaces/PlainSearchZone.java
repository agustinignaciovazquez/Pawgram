package ar.edu.itba.pawgram.model.interfaces;

import ar.edu.itba.pawgram.model.Location;
import ar.edu.itba.pawgram.model.User;

public interface PlainSearchZone {
    public long getId();
    public Location getLocation();
    public int getRange();
}
