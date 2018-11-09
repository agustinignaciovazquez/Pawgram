package ar.edu.itba.pawgram.service.utils;

import org.springframework.stereotype.Component;

@Component
public class HaversineDistance {
    private static final int EARTH_RADIUS = 6371*1000; // Approx Earth radius in M

    public double distance(double startLat, double startLong,
                                  double endLat, double endLong) {

        double dLat  = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat   = Math.toRadians(endLat);

        double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // <-- d
    }

    public double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}
