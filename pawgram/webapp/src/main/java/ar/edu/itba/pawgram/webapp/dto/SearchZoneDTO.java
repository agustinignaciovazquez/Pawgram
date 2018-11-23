package ar.edu.itba.pawgram.webapp.dto;

import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.SearchZone;
import ar.edu.itba.pawgram.model.User;

import java.net.URI;
import java.util.Optional;

public class SearchZoneDTO {
    private long id;
    private LocationDTO locationDTO;
    private int range;
    private UserDTO user;
    private URI url;

    public SearchZoneDTO(){

    }

    public SearchZoneDTO(final SearchZone sz, final URI baseUri, final Optional<User> loggedUser) {
        this.id = sz.getId();
        this.locationDTO = new LocationDTO(sz.getLocation(),baseUri);
        this.user = new UserDTO(sz.getUser(),baseUri);
        this.range = sz.getRange();
        this.url = baseUri.resolve("search_zones/"+id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocationDTO getLocationDTO() {
        return locationDTO;
    }

    public void setLocationDTO(LocationDTO locationDTO) {
        this.locationDTO = locationDTO;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }
}
