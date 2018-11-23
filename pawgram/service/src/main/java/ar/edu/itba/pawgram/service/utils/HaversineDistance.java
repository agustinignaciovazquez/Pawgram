package ar.edu.itba.pawgram.service.utils;

import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.structures.Location;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public Post setPostDistance(Post p, Location currentLocation){
        Double distance = distance(currentLocation.getLatitude(),currentLocation.getLongitude(),
                p.getLocation().getLatitude(),p.getLocation().getLongitude());
        p.setDistance(distance.intValue());
        return p;
    }

    public List<Post> setPostsDistance(List<Post> posts, Location currentLocation){
        for(Post p: posts){
            setPostDistance(p,currentLocation);
        }
        return posts;
    }
}
