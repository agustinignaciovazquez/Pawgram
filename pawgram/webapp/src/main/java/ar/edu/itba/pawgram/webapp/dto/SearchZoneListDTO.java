package ar.edu.itba.pawgram.webapp.dto;

import ar.edu.itba.pawgram.model.SearchZone;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

public class SearchZoneListDTO {
    private List<PlainSearchZoneDTO> searchzones;
    private long totalCount;
    private long count;

    public SearchZoneListDTO() {}

    public SearchZoneListDTO(final List<SearchZone> searchzones, long totalCount, final URI baseUri) {
        this.setTotalCount(totalCount);
        this.searchzones = new LinkedList<>();
        this.setCount(searchzones.size());

        for (SearchZone sz : searchzones)
            this.searchzones.add(new PlainSearchZoneDTO(sz, baseUri));
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
